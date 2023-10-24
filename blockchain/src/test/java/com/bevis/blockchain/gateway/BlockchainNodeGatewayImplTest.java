package com.bevis.blockchain.gateway;

import com.bevis.blockchain.gateway.dto.BalanceResponse;
import com.bevis.blockchain.gateway.dto.TxListResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application.yml")
public class BlockchainNodeGatewayImplTest {

    @Autowired
    private BlockchainNodeGatewayImpl blockchainNodeGateway;

    @Test
    public void testGetBalance() {
        BalanceResponse response = blockchainNodeGateway.getBalance("BCH");
        assert response.getBalance() != null;
    }

    @Ignore
    @Test
    public void testProcessTx() {
        blockchainNodeGateway.processTx("BCH", "qqcx44f9dgl4p6y33ux7htltvl59jfa60y4y97e3et", "My test 11");
    }

    @Test
    public void testGetTxList() {
        TxListResponse txList = blockchainNodeGateway.getTxList("BCH", "qzah8jkpjfcxss2lmvallvlpxw3pcuywmqvh6f8p4g");
        assert txList != null;
    }
}
