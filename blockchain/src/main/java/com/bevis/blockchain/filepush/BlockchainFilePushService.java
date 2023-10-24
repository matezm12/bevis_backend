package com.bevis.blockchain.filepush;

import com.bevis.blockchain.cryptopay.dto.Transaction;

public interface BlockchainFilePushService {
    Transaction pushBevisCertToBlockchain(String publicKey, String blockchain, String data);
    Transaction pushBevisDataToBlockchain(String publicKey, String blockchain, String data);
    Transaction pushDataToBlockchain(String publicKey, String blockchain, String data);
}
