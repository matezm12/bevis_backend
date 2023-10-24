package com.bevis.assetimport.impl;

import com.bevis.assetimport.AssetImportDtoMapper;
import com.bevis.assetimport.AssetImportException;
import com.bevis.assetimport.AssetUnImportService;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.asset.DynamicAssetException;
import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Master;
import com.bevis.assetimport.repository.AssetImportRepository;
import com.bevis.asset.DynamicAssetService;
import com.bevis.master.MasterPermissionCheckingService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bevis.assetimport.util.BarcodeUtil.getAssetsFromBarcode;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class AssetUnImportServiceImpl implements AssetUnImportService {

    private final MasterService masterService;
    private final DynamicAssetService dynamicAssetService;
    private final AssetImportDtoMapper assetImportDtoMapper;
    private final AssetImportRepository assetImportRepository;
    private final MasterPermissionCheckingService masterPermissionCheckingService;

    @Override
    public AssetImport undoAssetImport(AssetImport assetImport) throws AssetImportException {
        final String barcode = assetImport.getBarcode();
        final List<String> barcodeItems = assetImportDtoMapper.fromBarcode(barcode);
        List<String> publicKeys = getAssetsFromBarcode(barcodeItems);
        final List<Master> masters = findAllMastersByPublicKeys(publicKeys)
                .stream()
                .filter(master -> Objects.equals(master.getCodereadrScanId(), assetImport.getScanId()))
                .collect(Collectors.toList());
        unImportMasters(masters);
        assetImport.setMatched(false);
        assetImport.setCancelledAt(Instant.now());
        AssetImport result = assetImportRepository.saveAndFlush(assetImport);
        log.debug("Successfully Un-imported AssetImport ScanID:{}", result.getScanId());
        return result;
    }

    @Override
    public void undoMastersByScanIdAndPublicKeyNotIn(String scanId, List<String> newMasters) throws AssetImportException {
        List<Master> oldMasters = masterService.findAllByScanId(scanId);
        List<Master> mastersToUnImport = oldMasters.stream()
                .filter(master -> !newMasters.contains(master.getPublicKey()) &&
                        Objects.equals(master.getCodereadrScanId(), scanId))
                .collect(Collectors.toList());
        unImportMasters(mastersToUnImport);
    }

    private void unImportMasters(List<Master> masters) throws AssetImportException {
        try {
            if (masters.isEmpty()) {
                log.debug("Nothing to un-import");
            }
            masterPermissionCheckingService.validateAssetImportPermission(masters);
            log.debug("Public keys prepared to unassigned: {}", mapPublicKeysFromMaster(masters));
            for (Master master : masters) {
                log.trace("Un-importing master: {}", master.getId());
                unImportMaster(master);
            }
            masterService.saveAll(masters);
        } catch (PermissionDeniedException | DynamicAssetException e) {
            throw new AssetImportException("Error undo asset import with cause: " + e.getMessage(), e);
        }
    }

    private void unImportMaster(Master master) throws DynamicAssetException {
        dynamicAssetService.deleteByMaster(master);
        master.setCodereadrScanId(null);
        master.setOwnerAssetId(null);
    }

    private List<String> mapPublicKeysFromMaster(List<Master> masters) {
        return masters.stream()
                .map(Master::getPublicKey)
                .collect(Collectors.toList());
    }

    private List<Master> findAllMastersByPublicKeys(List<String> publicKeys) {
        return masterService.findAllByPublicKeyIn(publicKeys);
    }
}
