package com.bevis.master.impl;

import com.bevis.assettype.AssetTypesService;
import com.bevis.assettype.domain.AssetType;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.config.ConfigService;
import com.bevis.keygen.CryptoKeyDTO;
import com.bevis.keygen.CryptoKeyGeneratorService;
import com.bevis.keygen.KeygenException;
import com.bevis.blockchaincore.BlockchainService;
import com.bevis.master.MasterImportService;
import com.bevis.master.MasterService;
import com.bevis.master.domain.Master;
import com.bevis.master.domain.MasterImport;
import com.bevis.master.dto.AssetKeyCsvDTO;
import com.bevis.master.dto.MasterImportCsvDataDTO;
import com.bevis.master.dto.MasterImportDTO;
import com.bevis.master.dto.MasterImportRequest;
import com.bevis.master.mapper.MasterAssetKeysMapper;
import com.bevis.master.mapper.MasterImportMapper;
import com.bevis.master.repository.MasterImportRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.bevis.common.util.DateUtil.convertInstantToLocalDate;
import static com.bevis.config.ConfigKey.DEFAULT_BLOCKCHAIN;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class MasterImportServiceImpl implements MasterImportService {

    private final MasterImportRepository masterImportRepository;
    private final CryptoKeyGeneratorService cryptoKeyGeneratorService;
    private final MasterImportMapper masterImportMapper;
    private final MasterAssetKeysMapper masterAssetKeysMapper;
    private final AssetTypesService assetTypesService;
    private final BlockchainService blockchainService;
    private final MasterService masterService;
    private final ConfigService configService;

    @Override
    public MasterImportDTO generateMasters(MasterImportRequest request) {
        MasterImport masterImport = new MasterImport();
        masterImport.setName(request.getName());
        @NonNull final String keyType = request.getKeyType();
        masterImport.setKeyType(keyType);
        @NonNull Integer quantity = request.getQty();
        masterImport.setQty(quantity);
        final Instant date = Instant.now();
        masterImport.setDate(date);
        try {
            String blockchainName = keyType.toUpperCase();
            Blockchain blockchain = blockchainService.findByName(blockchainName)
                    .orElseThrow(() -> new ObjectNotFoundException("Blockchain not found"));

            final Map<String, CryptoKeyDTO> cryptoKeys = generateUniqueAssetKeys(blockchain, quantity);
            Set<Master> masters = new HashSet<>();


            for (Map.Entry<String, CryptoKeyDTO> entry : cryptoKeys.entrySet()) {
                String assetId = entry.getKey();
                CryptoKeyDTO cryptoKey = entry.getValue();
                masters.add(Master.builder()
                        .genDate(date)
                        .publicKey(cryptoKey.getAddress())
                        .id(assetId)
                        .blockchain(blockchain)
                        .masterImport(masterImport)
                        .isActive(true)
                        .build());
            }
            masterImport.setMasters(masters);
            masterImport.setGenStatus(true);
        } catch (KeygenException e) {
            masterImport.setGenStatus(false);
            throw e;
        }
        masterImportRepository.save(masterImport);
        return masterImportMapper.toDto(masterImport);
    }

    private Map<String, CryptoKeyDTO> generateUniqueAssetKeys(Blockchain blockchain, @NonNull Integer quantity) {
        Map<String, CryptoKeyDTO> cryptoKeysMap = new HashMap<>();

        final int BATCH_MAX_SIZE = 1000;

        while (cryptoKeysMap.size() < quantity) {
            if (cryptoKeysMap.size() > 0) {
                log.warn("Generating additional keys cause duplicate collisions");
            }

            int currentQuantity = quantity - cryptoKeysMap.size();
            String blockchainName = blockchain.getName().toUpperCase();
            List<CryptoKeyDTO> generatedKeys = cryptoKeyGeneratorService.generate(blockchainName, Math.min(currentQuantity, BATCH_MAX_SIZE));

            Map<String, CryptoKeyDTO> cryptoKeysSubMap = new HashMap<>();
            for (CryptoKeyDTO cryptoKeyDTO : generatedKeys) {
                String publicKey = cryptoKeyDTO.getAddress();
                String assetId = blockchainService.fetchAssetIdFromPublicKey(publicKey, blockchain);
                cryptoKeysSubMap.put(assetId, cryptoKeyDTO);
            }
            final Set<String> newKeys = cryptoKeysSubMap.keySet();

            final Set<String> dbDuplicates = masterService.findAllByAssetIdIn(new ArrayList<>(newKeys)).stream()
                    .map(Master::getId).collect(Collectors.toSet());


            final Set<String> filteredKeys = newKeys.stream()
                    .filter(x -> !cryptoKeysMap.containsKey(x))
                    .filter(x -> !dbDuplicates.contains(x))
                    .collect(Collectors.toSet());


            final Map<String, CryptoKeyDTO> keysToInsert = cryptoKeysSubMap.entrySet().stream()
                    .filter(x -> filteredKeys.contains(x.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            cryptoKeysMap.putAll(keysToInsert);
        }
        return cryptoKeysMap;
    }

    @Override
    public MasterImportDTO updateMasters(MasterImportRequest request) {
        MasterImport masterImport = masterImportRepository.getOne(request.getId());
        masterImport.setName(request.getName());
        masterImportRepository.save(masterImport);
        return masterImportMapper.toDto(masterImport);
    }

    @Override
    public MasterImportCsvDataDTO getMasterImportAssetKeys(Long masterImportId) {
        final MasterImport masterImport = masterImportRepository.findById(masterImportId)
                .orElseThrow(() -> new ObjectNotFoundException("Master import with ID: " + masterImportId + " not found "));
        final Set<Master> masters = masterImport.getMasters();
        final List<AssetKeyCsvDTO> keys = masters.stream()
                .map(masterAssetKeysMapper::toDto).collect(Collectors.toList());
        final String fileName = String.format("Date:%s_vendorID:%s_qty:%s.csv",
                convertInstantToLocalDate(masterImport.getDate()),
                "-",
                masterImport.getQty() != null ? masterImport.getQty() : masters.size()
        );
        return new MasterImportCsvDataDTO(keys, fileName);
    }

    @Override
    public Master generateMasterOnDefaultBlockchain() {
        return generateBevisMasterByBlockchain(configService.getValue(DEFAULT_BLOCKCHAIN));
    }

    @Override
    public Master generateBevisMasterByBlockchain(String blockchainName) {
        return generateMasterByBlockchain(blockchainName, null);
    }

    @Override
    public Master generateMasterByBlockchain(String blockchainName, AssetType assetType) {
        final CryptoKeyDTO cryptoKey = cryptoKeyGeneratorService.generate(blockchainName);
        final Blockchain blockchain = blockchainService.findByName(blockchainName).orElse(null);
        final String publicKey = cryptoKey.getAddress();
        return masterService.saveMaster(
                Master.builder()
                        .genDate(Instant.now())
                        .publicKey(cryptoKey.getAddress())
                        .id(blockchainService.fetchAssetIdFromPublicKey(publicKey, blockchain))
                        .assetType(assetType)
                        .blockchain(blockchain)
                        .isActive(true)
                        .build()
        );
    }

    @Override
    public Page<MasterImport> findAll(Pageable pageable) {
        return masterImportRepository.findAll(pageable);
    }

    @Override
    public Page<MasterImport> findAllByNameContaining(String search, Pageable pageable) {
        return masterImportRepository.findAllByNameContaining(search, pageable);
    }

    @Override
    public MasterImport getOne(Long id) {
        return masterImportRepository.getById(id);
    }
}
