package com.bevis.balance.token

import com.bevis.balance.dto.CryptoToken
import com.bevis.balance.token.dto.Erc20DataResponse
import com.bevis.balance.token.dto.Page
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class Erc20TokenServiceImpl(private val erc20WebClient: Erc20WebClient) : Erc20TokenService {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun getWalletTokens(walletId: String, currency: String): List<CryptoToken> {
        val response = loadTokensByAddressFromApi(currency, walletId)
        val tokens = response.data
        return tokens.map {
            CryptoToken(
                address = it.address,
                logo = it.logo,
                name = it.name,
                currency = it.symbol,
                balance = it.balance!!.divide(BigDecimal.valueOf(10).pow(it.decimals!!.toInt())).toDouble()
            )
        }
    }

    private suspend fun loadTokensByAddressFromApi(chain: String, address: String): Erc20DataResponse {
        return try {
            erc20WebClient.loadTokensByAddress(
                chain = chain,
                address = address,
                page = Page(page = 0, pageSize = 500)
            )
        } catch (e: Exception) {
            log.error("Error loading ERC-20 tokens. Chain: $chain, address: $address, reason: ${e.message}")
            Erc20DataResponse()
        }
    }
}
