package com.bevis.blockchain.datasign;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class DataSignServiceImplTest {

    private DataSignServiceImpl transactionDataSignService;

    @Before
    public void init() {
        DataSignProps dataSignProps = new DataSignProps();
        dataSignProps.setSecret("RandomSecret");
        transactionDataSignService = new DataSignServiceImpl(dataSignProps);
    }

    @Test
    public void testSignTxData() {
        String test = "test";
        String address = "address";
        String result = transactionDataSignService.signTxData(test, address);
        assertNotNull(result);
    }

    @Test
    public void testValidateSignedData() {
        String signedData = "test.75d49.fa429c1a";
        String address = "address";
        boolean isCorrect = transactionDataSignService.validateSignedData(signedData, address);
        assertTrue(isCorrect);
    }

    @Test
    public void testSignTxData2() {
        String test = "Bevis.00.QmNibcTqBEZWNve3Kb185uG6BCBs5ya4nzACDficWDP2JB";
        String address = "qp52yc3cxuygc8tas4qjchp9l9xpextw6549k6frmr";
        String result = transactionDataSignService.signTxData(test, address);
        assertNotNull(result);
    }

    @Test
    public void testValidateSignedData2() {
        String signedData = "Bevis.00.QmNibcTqBEZWNve3Kb185uG6BCBs5ya4nzACDficWDP2JB.10fe2.a517e85e";
        String address = "qp52yc3cxuygc8tas4qjchp9l9xpextw6549k6frmr";
        boolean isCorrect = transactionDataSignService.validateSignedData(signedData, address);
        assertTrue(isCorrect);
    }
}
