package com.bevis.master.impl;

import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.master.domain.Master;
import com.bevis.blockchaincore.BlockchainService;
import com.bevis.master.MasterGeneratingService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class MasterGeneratingServiceImpl implements MasterGeneratingService {

    private final MasterService masterService;
    private final BlockchainService blockchainService;

    @Transactional(noRollbackFor = ObjectNotFoundException.class)
    @Override
    public List<Master> generateMasters(List<String> publicKeys, String cryptoCurrency, Instant scanTime) {
        Blockchain blockchain = blockchainService.findByName(cryptoCurrency)
                .orElseThrow(() -> new ObjectNotFoundException("CryptoCurrency not specified"));
        log.trace("blockchain: {}", blockchain.getName());
        return generateMasters(publicKeys, blockchain, scanTime);
    }

    private List<Master> generateMasters(List<String> publicKeys, Blockchain blockchain, Instant scanTime) {
        List<Master> generatedMasters = publicKeys.stream()
                .map(publicKey -> masterService.findFirstByPublicKey(publicKey)
                        .orElseGet(() -> generateMaster(scanTime, blockchain, publicKey))
                )
                .peek(master -> master.setBlockchain(blockchain))
                .collect(Collectors.toList());

        masterService.saveAll(generatedMasters);
        log.debug("New masters generated");
        return generatedMasters;
    }

    private Master generateMaster(Instant scanTime, Blockchain blockchain, String publicKey) {
        Master master = new Master();
        master.setId(blockchainService.fetchAssetIdFromPublicKey(publicKey, blockchain));
        master.setPublicKey(publicKey);
        master.setGenDate(scanTime);
        log.trace("Master created: {}", master);
        return master;
    }
}
