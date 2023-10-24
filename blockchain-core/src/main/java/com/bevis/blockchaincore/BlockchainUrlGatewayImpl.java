package com.bevis.blockchaincore;

import com.bevis.blockchaincore.domain.Blockchain;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BlockchainUrlGatewayImpl implements BlockchainUrlGateway {

    private final BlockchainRepository blockchainRepository;
    @Override
    public String getBlockchainAddressLink(String publicKey, Blockchain blockchain) {
        if (blockchain == null || publicKey == null)
            return Strings.EMPTY;
        return String.format(blockchain.getAddressUrl(), publicKey);
    }

    @Override
    public String getBlockchainTransactionLink(String transactionId, Blockchain blockchain) {
        if (blockchain == null || transactionId == null)
            return Strings.EMPTY;
        return String.format(blockchain.getExplorerUrl(), transactionId);
    }

    @Override
    public String getBlockchainTransactionLink(String transactionId, String blockchainName) {
        String explorerUrl = blockchainRepository.findByNameIgnoreCase(blockchainName)
                .map(Blockchain::getExplorerUrl).orElse(null);
        return Objects.nonNull(explorerUrl) ? String.format(explorerUrl, transactionId): Strings.EMPTY;
    }
}
