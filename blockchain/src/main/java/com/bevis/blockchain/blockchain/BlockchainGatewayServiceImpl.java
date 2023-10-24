package com.bevis.blockchain.blockchain;

import com.bevis.blockchain.blockchain.dto.Transaction;
import com.bevis.blockchain.blockchain.dto.TransactionStatus;
import com.bevis.blockchain.gateway.BlockchainNodeGateway;
import com.bevis.blockchain.gateway.dto.BalanceResponse;
import com.bevis.blockchain.gateway.dto.TransactionResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class BlockchainGatewayServiceImpl implements BlockchainGatewayService {

    public static final int MAXIMUM_SIZE = 10000;
    public static final int EXPIRATION_DURATION = 15;

    private Cache<String, String> balanceCache;

    private final BlockchainNodeGateway blockchainNodeGateway;

    @PostConstruct
    void init() {
        this.balanceCache = Caffeine.newBuilder()
                .expireAfterWrite(EXPIRATION_DURATION, TimeUnit.SECONDS)
                .maximumSize(MAXIMUM_SIZE)
                .build();
    }

    @Override
    public String getBalance(String blockchain) {
        return Optional.ofNullable(balanceCache.getIfPresent(blockchain))
                .orElseGet(() -> {
                    String balance = loadBalance(blockchain);
                    balanceCache.put(blockchain, balance);
                    return balance;
                });
    }

    @Override
    public List<Transaction> loadTransactionsByAddress(String blockchain, String publicKey) {
        return blockchainNodeGateway.getTxList(blockchain, publicKey).stream()
                .map(tx -> Transaction.builder()
                        .transactionId(tx.getTx())
                        .data(tx.getData())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public TransactionStatus getTransactionStatus(String blockchain, String transactionId) {
        TransactionResponse transactionResponse = blockchainNodeGateway.getTransactionStatus(blockchain, transactionId);
        return TransactionStatus.builder()
                .transactionId(transactionResponse.getTransactionId())
                .transactionBlock(transactionResponse.getBlockHeight())
                .confirmationsCount(transactionResponse.getConfirmations())
                .build();
    }

    private String loadBalance(String blockchain) {
        try {
            BalanceResponse balance = blockchainNodeGateway.getBalance(blockchain);
            return String.valueOf(balance.getBalance());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.ofNullable(e.getMessage())
                    .orElse("N/A");
        }
    }

}
