package com.bevis.blockchainfile;

import com.bevis.blockchain.blockchain.dto.Transaction;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class TransactionUtilTest {
    @Test
    public void testGetTxIndex() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("asdasdasdasd");
        transaction.setData("Bevis.05.Qmdsdfsdfdsfdsfsdfds.asdasdasdasd");
        int txIndex = TransactionUtil.getTxIndex(transaction);
        assertEquals(5, txIndex);
    }
}
