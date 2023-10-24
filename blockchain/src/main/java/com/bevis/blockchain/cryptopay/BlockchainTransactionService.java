package com.bevis.blockchain.cryptopay;

import com.bevis.blockchain.cryptopay.dto.Transaction;
import com.bevis.blockchain.cryptopay.dto.TransactionRequest;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface BlockchainTransactionService {
    Transaction process(@Valid TransactionRequest transactionRequest);
}
