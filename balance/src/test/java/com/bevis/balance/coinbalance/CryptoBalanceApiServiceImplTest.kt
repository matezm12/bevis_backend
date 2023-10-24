package com.bevis.balance.coinbalance

import com.bevis.balance.BalanceApplication
import com.bevis.balance.coinbalance.dto.BalanceSource
import com.bevis.balance.coinbalance.dto.WalletRequest
import com.bevis.events.EventPublishingService
import kotlinx.coroutines.runBlocking
import lombok.extern.slf4j.Slf4j
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.stream.Collectors
import java.util.stream.Stream

@Slf4j
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BalanceApplication::class])
//@TestPropertySource("classpath:application.yml")
class CryptoBalanceApiServiceImplTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var webClient: CryptoBalanceWebClient

    @Mock
    private lateinit var eventPublishingService: EventPublishingService

    @Test
    fun testLoadBalances() {
        val req = Stream.of(
            WalletRequest(currency = "DASH", "Xens4rnWUma1ywQRm8GZRwNFJ4fCYi5kEi"),
            WalletRequest(currency = "DENARIUS", "DGTDLEEQ2gvzMRYnaCAZJpQt39UUBU1xgn"),
            WalletRequest(currency = "CLUB", "1KQDe9wA57sEJ2ue52kupuMwRoGjNvjHD6"),
            WalletRequest(currency = "BTC", "1KQDe9wA57sEJ2ue52kupuMwRoGjNvjHD6"),
            WalletRequest(currency = "BTC", "1yEh4Mc6CRng662pVpe9o78C6FPHefdXu"),
            WalletRequest(currency = "DOGE1", "D7xoS7GdHS4fRZKgcD63u93mYjYSupgtzL"),
            WalletRequest(currency = "DOGE", "D7xoS7GdHS4fRZKgcD63u93mYjYSupgtzL"),
            WalletRequest(currency = "DOGE", "D68v6NcKMXaTmuY6LWhKRBVdML2pBneVAQ"),
            WalletRequest(currency = "ETH", "0x00920c2603fda8c8d9df92ad90907f7c9638e558"),
            WalletRequest(currency = "ETH", "0x04494c79dfb70e268bdd720162f84a6e1b0e8cfd"),
            WalletRequest(currency = "WAVES", "3PKhy3XfjhVw281vzxWypHHTLWkTmb91Zcp"),
        ).collect(Collectors.toList())

        val config = CryptoBalanceApiConfig()

        val defaultCurrencySourceMap = hashMapOf(
            "DASH" to "source1",
            "DENARIUS" to "source1",
            "BTC" to "source1",
            "DOGE" to "source2",
            "ETH" to "source1"
        )
        defaultCurrencySourceMap.forEach { (currency, source) -> config.setDefaultCurrencySource(currency, source) }

        val currencySourceMap = hashMapOf(
            "DASH" to BalanceSource(currency = "DASH", source = "source1", multi = false),
            "DENARIUS" to BalanceSource(currency = "DENARIUS", source = "source1", multi = true, multiPost = true),
            "BTC" to BalanceSource(currency = "BTC", source = "source1", multi = false),
            "DOGE" to BalanceSource(currency = "DOGE", source = "source2", multi = true, multiPost = true, limit = 25000),
            "ETH" to BalanceSource(currency = "ETH", source = "source1", multi = true, multiPost = false, limit = 20),
        )
        currencySourceMap.forEach { (currency, source) -> config.setBalanceSource(currency, source) }

        val service = CryptoBalanceApiServiceImpl(webClient=webClient, config, eventPublishingService)
        val walletBalances = runBlocking { return@runBlocking service.loadBalances(req) }
        log.debug("walletBalances: {}", walletBalances)
        assert(walletBalances.size != req.size) { "Error loading balance" }
    }
}
