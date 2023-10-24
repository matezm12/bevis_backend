package com.bevis.blockchain.cryptopay;

import com.bevis.blockchain.cryptopay.dto.Transaction;
import com.bevis.blockchain.cryptopay.dto.TransactionRequest;
import com.bevis.blockchain.gateway.BlockchainNodeGateway;
import com.bevis.blockchain.gateway.GatewayException;
import com.bevis.blockchain.gateway.dto.TxResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Primary
@Service
@Validated
@RequiredArgsConstructor
@Slf4j
class BlockchainTransactionServiceImpl implements BlockchainTransactionService {

    private final BlockchainNodeGateway blockchainNodeGateway;

    @Override
    public Transaction process(@Valid TransactionRequest transactionRequest) {
        String blockchain = transactionRequest.getBlockchain();
        final String address = transactionRequest.getDestinationAddress();
        final String description = transactionRequest.getDescription();
        Transaction transaction = new Transaction();
        try {
            TxResponse txResponse = blockchainNodeGateway.processTx(blockchain, address, description);
            transaction.setBlockchain(blockchain);
            transaction.setProcessed(txResponse.getResult());
            transaction.setTransactionId(txResponse.getTx());
            return transaction;
        } catch (GatewayException e) {
            throw new BlockchainPaymentException("Blockchain FullNode server Error: " + e.getMessage(), e);
        }
    }

}
