package com.bevis.blockchain.blockchain;

import com.bevis.blockchain.blockchain.dto.Transaction;
import com.bevis.blockchain.blockchain.dto.TransactionStatus;

import java.util.List;

public interface BlockchainGatewayService {
    String getBalance(String blockchain);
    List<Transaction> loadTransactionsByAddress(String blockchain, String publicKey);
    TransactionStatus getTransactionStatus(String blockchain, String transactionId);
}
