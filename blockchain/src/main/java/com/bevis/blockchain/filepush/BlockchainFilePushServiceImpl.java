package com.bevis.blockchain.filepush;

import com.bevis.blockchain.cryptopay.BlockchainTransactionService;
import com.bevis.blockchain.cryptopay.dto.Transaction;
import com.bevis.blockchain.cryptopay.dto.TransactionRequest;
import com.bevis.blockchain.datasign.DataSignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
class BlockchainFilePushServiceImpl implements BlockchainFilePushService {

    private static final String BEVIS_DATA_PREFIX = "Bevis.";

    private final BlockchainTransactionService blockchainTransactionService;
    private final DataSignService dataSignService;

    @Override
    public Transaction pushBevisCertToBlockchain(String publicKey, String blockchain, String data) {
        String bevisData = BEVIS_DATA_PREFIX + "00." + data;
        return pushDataToBlockchain(publicKey, blockchain, bevisData);
    }

    @Override
    public Transaction pushBevisDataToBlockchain(String publicKey, String blockchain, String data) {
        String bevisData = BEVIS_DATA_PREFIX + data;
        return pushDataToBlockchain(publicKey, blockchain, bevisData);
    }

    @Override
    public Transaction pushDataToBlockchain(String publicKey, String blockchain,  String data) {
        String signedData = dataSignService.signTxData(data, publicKey);
        final TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setDestinationAddress(publicKey);
        transactionRequest.setAmountToPay(BigDecimal.ZERO);
        transactionRequest.setDescription(signedData);
        transactionRequest.setBlockchain(blockchain);
        final Transaction transaction = blockchainTransactionService.process(transactionRequest);
        final String transactionId = transaction.getTransactionId();
        log.debug("Transaction ID: {}", transactionId);
        return transaction;
    }
}
