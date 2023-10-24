package com.bevis.assetinfo.impl;

import com.bevis.assetinfo.LogoFetchingService;
import com.bevis.assetinfo.mapper.AssetInfoMapper;
import com.bevis.filecore.domain.File;
import com.bevis.filecode.domain.FileCode;
import com.bevis.master.domain.Master;
import com.bevis.filecode.domain.enumeration.FileCodeGroup;
import com.bevis.asset.DynamicAssetService;
import com.bevis.filecode.FileCodeService;
import com.bevis.gateway.UrlGatewayService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LogoFetchingServiceImpl implements LogoFetchingService {

    private final static String SKU_FIELD = "sku";

    @Value("${common.imagePlaceholder}")
    private String defaultImageIpfs;

    private final MasterService masterService;
    private final UrlGatewayService urlGatewayService;
    private final DynamicAssetService dynamicAssetService;
    private final FileCodeService fileCodeService;
    private final AssetInfoMapper assetInfoMapper;

    @Override
    public String loadFileUrlByMaster(Master master) {
        File file = loadFileByMaster(master).orElse(null);
        if (Objects.isNull(file)) {
            return getDefaultImage();
        }
        FileCode fileCode = fileCodeService.getFileCodeByFileType(file.getFileType()).orElse(null);
        return mapLogo(file, fileCode);
    }

    private String mapLogo(File file, FileCode fileCode) {
        if (Objects.nonNull(file)) {
            if (Objects.nonNull(fileCode) && fileCode.getFileGroup() != FileCodeGroup.IMAGE) {
                String ipfs = file.getEncrypted() ? fileCode.getEncryptedPlaceholder() : fileCode.getPlaceholder();
                return urlGatewayService.getIpfsUrl(ipfs);
            }
            return Optional.of(file)
                .map(File::getIpfs)
                .map(assetInfoMapper::getIpfsUrl)
                .orElse(getDefaultImage());
        }
        return getDefaultImage();
    }

    private Optional<File> loadFileByMaster(Master master) {
        if (Objects.nonNull(master.getFile())) {
            return Optional.of(master.getFile());
        }
        return loadFileByMasterProduct(master);
    }

    private Optional<File> loadFileByMasterProduct(Master master) {
        return dynamicAssetService.getDynamicFieldsById(master.getId())
                .entrySet()
                .stream()
                .filter(x -> Objects.equals(x.getKey(), SKU_FIELD) && Objects.nonNull(x.getValue()))
                .map(Map.Entry::getValue)
                .filter(x -> Objects.nonNull(x.getValue()))
                .map(x->String.valueOf(x.getValue()))
                .findFirst()
                .flatMap(masterService::findById)
                .map(Master::getFile);
    }

    private String getDefaultImage() {
        return assetInfoMapper.getIpfsUrl(defaultImageIpfs);
    }
}
