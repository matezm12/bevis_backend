package com.bevis.assetimport.impl;

import com.bevis.assetimport.AssetImportException;
import com.bevis.assetimport.AssetImportGatewayService;
import com.bevis.assetimport.AssetImportService;
import com.bevis.assetimport.AssetUnImportService;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.AssetImportFilter;
import com.bevis.assetimport.mapper.DynamicAssetImportMapper;
import com.bevis.assetimport.repository.AssetImportRepository;
import com.bevis.common.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.bevis.assetimport.impl.AssetImportSpecification.bySearchQuery;
import static com.bevis.security.util.SecurityUtils.checkPermissions;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class AssetImportServiceImpl implements AssetImportService {

    private final AssetImportRepository assetImportRepository;
    private final AssetImportGatewayService assetImportGatewayService;
    private final DynamicAssetImportMapper dynamicAssetImportMapper;
    private final AssetUnImportService assetUnImportService;

    @Override
    public AssetImport findOne(Long id) {
        return assetImportRepository.findById(id)
                .map(x -> checkPermissions(x, x.getVendorAssetId()))
                .orElseThrow(() -> new ObjectNotFoundException("Asset import not found with ID: " + id));
    }

    @Override
    public Page<AssetImport> findAll(AssetImportFilter filter, Pageable pageable) {
        return assetImportRepository.findAll(bySearchQuery(filter), pageable);
    }

    @Override
    public AssetImport importAssets(AssetImportDTO assetImportDTO) throws AssetImportException {
        return assetImportGatewayService.importAssets(assetImportDTO);
    }

    @Override
    public AssetImport reImportAssets(AssetImport assetImport) throws AssetImportException {
        AssetImportDTO assetImportDTO = dynamicAssetImportMapper.mapFromEntity(assetImport);
        List<String> newMasters = assetImportDTO.getBarcodeItems();
        assetUnImportService.undoMastersByScanIdAndPublicKeyNotIn(assetImportDTO.getScanId(), newMasters);
        return importAssets(assetImportDTO);
    }

    @Override
    public AssetImport undoAssetImport(Long id) throws AssetImportException {
        final AssetImport assetImport = findOne(id);
        return assetUnImportService.undoAssetImport(assetImport);
    }

    @Override
    public void deleteById(Long id) throws AssetImportException {
        AssetImport assetImport = assetImportRepository.findById(id).orElse(null);
        if (Objects.nonNull(assetImport)) {
            if (!assetImport.getMatched()) {
                assetImportRepository.deleteById(id);
                log.debug("AssetImported: {} removed!", id);
            } else {
                throw new AssetImportException("Cannot remove asset import");
            }
        }
    }

}
