package com.bevis.balance.coinbalance

import com.bevis.balance.coinbalance.dto.Balance
import com.bevis.balance.coinbalance.dto.BalanceSource
import com.bevis.balance.coinbalance.dto.DeadSourceEvent
import com.bevis.balance.coinbalance.dto.WalletRequest
import com.bevis.common.util.pmap
import com.bevis.events.EventPublishingService
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class CryptoBalanceApiServiceImpl(
    private val webClient: CryptoBalanceWebClient,
    private val apiConfig: CryptoBalanceApiConfig,
    private val eventPublishingService: EventPublishingService
) :
    CryptoBalanceApiService {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun loadSingleBalance(currency: String, address: String): Balance? {
        return loadSingleBalance(currency, address, getDefaultSource(currency))
    }

    override suspend fun loadSingleBalance(currency: String, address: String, source: String): Balance? {
        return try {
            webClient.loadSingleBalance(currency, address, source)
        } catch (e: BalanceException) {
            handleBalanceException(currency, source, e)
            null
        } catch (e: Exception) {
            log.error("Error loading balances for Currency: {}, Source: {}, Reason: {}", currency, source, e.message)
            null
        }
    }

    override suspend fun loadBalances(wallets: List<WalletRequest>): List<Balance> {
        val walletGroups = wallets.groupBy(keySelector = { it.currency }, valueTransform = { it.address })
        return walletGroups.entries
            .pmap { walletGroup -> loadBalances(currency = walletGroup.key, addresses = walletGroup.value) }
            .flatten()
    }

    override suspend fun loadBalances(currency: String, addresses: List<String>): List<Balance> {
        return try {
            val source = getDefaultSource(currency = currency)
            loadBalances(currency = currency, addresses = addresses, source = source)
        } catch (e: Exception) {
            log.error("Error loading balances for Currency: {}, Reason: {}", currency, e.message)
            emptyList()
        }
    }

    override suspend fun loadBalances(currency: String, addresses: List<String>, source: String): List<Balance> {
        try {
            val sourceInfo = getSourceInfo(currency, source)
            return if (addresses.size > 1 && sourceInfo.multi) {
                val limit = sourceInfo.limit ?: addresses.size //if limit is null then full size
                addresses.chunked(limit)
                    .pmap { addressesChunk -> loadMultiBalances(currency, addressesChunk, sourceInfo) }
                    .flatten()
            } else { //multi not supported
                addresses.pmap { loadSingleBalance(currency, it, source) }.filterNotNull()
            }
        } catch (e: BalanceException) {
            handleBalanceException(currency, source, e)
            return emptyList()
        } catch (e: Exception) {
            log.error("Error loading balances for Currency: {}, Source: {}, Reason: {}", currency, source, e.message)
            return emptyList()
        }
    }

    private suspend fun loadMultiBalances(currency: String, addresses: List<String>, sourceInfo: BalanceSource) =
        if (sourceInfo.multiPost == null) {//Method type: NOT DEFINED
            webClient.loadMultiBalancesDefault(currency, addresses, sourceInfo.source).data
        } else if (sourceInfo.multiPost) {//Method type: POST
            webClient.loadMultiBalancesPost(currency, addresses, sourceInfo.source).data
        } else { //Method type: GET
            webClient.loadMultiBalancesGet(currency, addresses, sourceInfo.source).data
        }

    private fun handleBalanceException(currency: String, source: String, e: BalanceException) {
        log.error(
            "Error loading balances. Currency: {}, Source: {}, Status code: {}, Cause: {}",
            currency,
            source,
            e.statusCode,
            e.message
        )
        if (e.statusCode == 500) {
            eventPublishingService.publishEvent(
                DeadSourceEvent(errorMessage = e.message, currency = currency, source = source)
            )
        }
    }

    private fun getSourceInfo(currency: String, source: String): BalanceSource = try {
        apiConfig.getBalanceSource(currency, source)!!
    } catch (e: Exception) {
        log.error("Source {} not defined for currency: {}", source, currency)
        throw Exception("Source not defined!")
    }

    private fun getDefaultSource(currency: String): String = try {
        apiConfig.getDefaultCurrencySource(currency)!!
    } catch (e: Exception) {
        log.error("Default source not defined for currency: {}!", currency)
        throw Exception("Default source not defined!")
    }
}
