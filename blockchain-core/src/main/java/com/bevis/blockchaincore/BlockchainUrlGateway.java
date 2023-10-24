package com.bevis.blockchaincore;

import com.bevis.blockchaincore.domain.Blockchain;

public interface BlockchainUrlGateway {
    String getBlockchainAddressLink(String publicKey, Blockchain blockchain);

    String getBlockchainTransactionLink(String transactionId, Blockchain blockchain);

    String getBlockchainTransactionLink(String transactionId, String blockchainName);
}
