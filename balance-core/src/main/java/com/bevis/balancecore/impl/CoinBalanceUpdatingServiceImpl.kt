package com.bevis.balancecore.impl

import com.bevis.balance.CryptoCurrencyBalanceResolver
import com.bevis.balance.dto.Balance
import com.bevis.balancecore.BalanceUtil.shouldUpdateBalance
import com.bevis.balancecore.CoinBalanceService
import com.bevis.balancecore.CoinBalanceUpdatingService
import com.bevis.balancecore.domain.CoinBalance
import com.bevis.blockchaincore.BlockchainRepository
import com.bevis.blockchaincore.domain.Blockchain
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors


private const val BATCH_SIZE = 25

@Slf4j
@Service
@RequiredArgsConstructor
internal class CoinBalanceUpdatingServiceImpl(
    private val coinBalanceService: CoinBalanceService,
    private val blockchainRepository: BlockchainRepository,
    private val cryptoCurrencyBalanceResolver: CryptoCurrencyBalanceResolver
) : CoinBalanceUpdatingService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun updateCoinBalances() {
        val updatableBlockchains = blockchainRepository.findAllByBalanceUpdateTrue()
        runBlocking {
            log.info("Started job for updating coin balances...")
            updatableBlockchains.filter { blockchain: Blockchain -> Objects.nonNull(blockchain.jobBalanceSource) }
                .forEach { blockchain: Blockchain ->
                    launch { updateCoinBalancesForCurrency(blockchain.name, blockchain.jobBalanceSource) }
                }
        }
    }

    private suspend fun updateCoinBalancesForCurrency(currency: String, source: String) = coroutineScope {
        log.debug("Search addressed needed to update for currency: {}", currency)
        val coins = coinBalanceService.findAllCoinsByCurrency(currency)
            .filter { coin -> shouldUpdateBalance(coin) }
        val addresses = coins.map { obj: CoinBalance -> obj.publicKey }
        if (addresses.isNotEmpty()) {
            log.debug("Updating balance for currency: {}, addresses amount: {}", currency, addresses.size)
            val updatedBalanceMap = cryptoCurrencyBalanceResolver.getWalletBalances(currency, addresses, source)
                .filter { Objects.nonNull(it.value) && Objects.nonNull(it.divider) }
                .stream()
                .collect(Collectors.toMap({ obj: Balance -> obj.address }, Function.identity()))
            val updatedCoinBalances = coins.stream()
                .peek { coinBalance: CoinBalance ->
                    val balance = updatedBalanceMap[coinBalance.publicKey]
                    if (Objects.nonNull(balance)) {
                        coinBalance.balance = balance!!.value
                        coinBalance.divider = balance.divider
                        coinBalance.updatedAt = Instant.now()
                    }
                }.collect(Collectors.toList())
            saveAll(currency, updatedCoinBalances)
            log.info("Updated balances for currency: {}, updated amount: {}", currency, updatedCoinBalances.size)
        } else {
            log.debug("No addressed needed to update for currency: {}", currency)
        }
    }

    private suspend fun saveAll(currency: String, coinBalances: List<CoinBalance>) = coroutineScope {
        val batchSize = BATCH_SIZE
        log.debug("Saving balances for currency: {}, split batches with size: {}", currency, batchSize)
        coinBalances.chunked(batchSize)
            .forEachIndexed { index: Int, balancesBatch: List<CoinBalance> ->
                launch {
                    coinBalanceService.saveAll(balancesBatch)
                    log.debug("Updated balance: {}, batch index: {}, amount: {}", currency, index, balancesBatch.size)
                }
            }
    }


}
