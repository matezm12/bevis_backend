package com.bevis.bevisassetpush;

import com.bevis.bevisassetpush.dto.TransactionDataDTO;

public interface TransactionDataGenerator {
    String generateTransactionData(TransactionDataDTO transactionDataDTO);
}
