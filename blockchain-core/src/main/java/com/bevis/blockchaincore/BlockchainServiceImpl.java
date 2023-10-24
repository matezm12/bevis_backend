package com.bevis.blockchaincore;

import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.blockchaincore.dto.BlockchainUpdatedEvent;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.events.EventPublishingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bevis.blockchaincore.BlockchainSpecification.bySearchQuery;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class BlockchainServiceImpl implements BlockchainService {

    private final BlockchainRepository blockchainRepository;
    private final EventPublishingService eventPublishingService;

    @Override
    public List<Blockchain> findAll() {
        return blockchainRepository.findAll();
    }

    @Override
    public Page<Blockchain> findAll(Pageable pageable) {
        return blockchainRepository.findAll(pageable);
    }

    @Override
    public Page<Blockchain> searchAll(String search, Pageable pageable) {
        return blockchainRepository.findAll(bySearchQuery(search), pageable);
    }

    @Override
    public Optional<Blockchain> findById(Long id) {
        return blockchainRepository.findById(id);
    }

    @Override
    public Optional<Blockchain> findByName(String blockchainName) {
        return blockchainRepository.findByNameIgnoreCase(blockchainName);
    }

    @Override
    public String fetchAssetIdFromPublicKey(String publicKey, Blockchain blockchain) {
        if (Strings.isBlank(blockchain.getAssetIdRegex())) {
            throw new ObjectNotFoundException("AssetIdRegex not defined");
        }
        return fetchAssetIdFromPublicKeyByRegex(publicKey, blockchain.getAssetIdRegex())
                .orElseThrow(() -> new ObjectNotFoundException("Cannot generate AssetID from Public key"));
    }

    @Override
    public Blockchain save(Blockchain blockchain) {
        Blockchain save = blockchainRepository.save(blockchain);
        eventPublishingService.publishEvent(BlockchainUpdatedEvent.builder()
                .blockchain(save)
                .build());
        return save;
    }

    @Override
    public void delete(Long id) {
        blockchainRepository.deleteById(id);
    }

    @Override
    public void updateSource(String blockchainName, String balanceSource) {
        blockchainRepository.findByNameIgnoreCase(blockchainName)
                .ifPresent(blockchain -> {
                    blockchain.setBalanceSource(balanceSource);
                    save(blockchain);
                });
    }

    private Optional<String> fetchAssetIdFromPublicKeyByRegex(String publicKey, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(publicKey);
        if (matcher.matches()) {
            return Optional.ofNullable(matcher.group(1));
        }
        return Optional.empty();
    }
}
