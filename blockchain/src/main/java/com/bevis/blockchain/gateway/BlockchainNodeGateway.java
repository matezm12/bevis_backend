package com.bevis.blockchain.gateway;

import com.bevis.blockchain.gateway.dto.BalanceResponse;
import com.bevis.blockchain.gateway.dto.TransactionResponse;
import com.bevis.blockchain.gateway.dto.TxListResponse;
import com.bevis.blockchain.gateway.dto.TxResponse;

public interface BlockchainNodeGateway {
    BalanceResponse getBalance(String blockchain);

    TxResponse processTx(String blockchain, String address, String description);

    TxListResponse getTxList(String blockchain, String address);

    TransactionResponse getTransactionStatus(String blockchain, String transactionId);
}
