package com.bevis.blockchainfile;

import com.bevis.blockchain.blockchain.dto.Transaction;

public final class TransactionUtil {

    public static int compareTx(Transaction t1, Transaction t2) {
        int txIndex1 = getTxIndex(t1);
        int txIndex2 = getTxIndex(t2);
        return txIndex1 - txIndex2;
    }

    static boolean isFileTx(Transaction transaction){
        return getTxIndex(transaction) != 0;
    }

    public static boolean isCertificateTx(Transaction transaction){
        return getTxIndex(transaction) == 0;
    }

    static int getTxIndex(Transaction transaction) {
        String data = transaction.getData();
        String indexStr = data.split("\\.")[1];
        return Integer.parseInt(indexStr);
    }
}
