package com.bevis.assetimport.codereadrservices;

import com.bevis.assetimport.AssetImportException;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.domain.CodeReadrService;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.ImportWrappingDTO;
import com.bevis.assetimport.dto.ProductDTO;
import com.bevis.assetimport.repository.CodeReadrServiceRepository;
import com.bevis.assetimport.util.DynamicProductMapper;
import com.bevis.assettype.AssetTypesService;
import com.bevis.assettype.domain.AssetType;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.asset.product.ProductUpcService;
import com.bevis.master.MasterService;
import com.bevis.master.domain.Master;
import com.bevis.user.UserService;
import com.bevis.user.domain.User;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.bevis.assetimport.util.BarcodeUtil.getUpcFromBarcode;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
class AssetImportContextLoader {

    private final AssetTypesService assetTypesService;
    private final CodeReadrServiceRepository codeReadrServiceRepository;
    private final MasterService masterService;
    private final UserService userService;
    private final ProductUpcService productUpcService;

    @NotNull
    public AssetType loadAssetTypeByProduct(ProductDTO productDTO) throws AssetImportException {
        final AssetType productAssetType = productDTO.getAssetType();
        if (Objects.nonNull(productAssetType)) {
            AssetType assetType = productAssetType.getInstanceAssetType();
            if (Objects.isNull(assetType)) {
                throw new AssetImportException("Product assetType not found or is null.");
            }
            return assetType;
        } else {
            throw new AssetImportException("Product type AssetType not found or is null.");
        }
    }

    public AssetType loadAssetTypeByKey(String key) {
        return assetTypesService.findFirstByKey(key)
                .orElseThrow(ObjectNotFoundException::new);
    }

    public CodeReadrService fetchCodeReadrService(String serviceId) {
        return codeReadrServiceRepository.findByServiceId(serviceId)
                .orElseThrow(() -> new RuntimeException("Service " + serviceId + " not registered."));
    }

    public ProductDTO fetchProduct(AssetImport assetImport) {
        final String scanId = assetImport.getScanId();
        return Optional.ofNullable(assetImport.getUpc())
                .flatMap(this::getProductTypeByUpc)
                .orElseGet(() -> {
                    log.warn("ScanId: {}, can not find Product for this scan", scanId);
                    return null;
                });
    }

    public Optional<Master> getProductMaster(ProductDTO productDTO) {
        return Optional.ofNullable(productDTO)
                .map(ProductDTO::getAssetId)
                .flatMap(masterService::findById);
    }

    private Optional<ProductDTO> getProductTypeByUpc(String upc) {
        log.debug("Upc: {} detected. Trying to find product by UPC", upc);
        return Optional.ofNullable(upc)
                .flatMap(productUpcService::findById)
                .flatMap(productUpc -> masterService.findById(productUpc.getAssetId()))
                .map(DynamicProductMapper::mapProductDto);
    }

    public String loadVendorAssetId(ImportWrappingDTO assetImportDTO) {
        String username = Optional.ofNullable(assetImportDTO)
                .map(ImportWrappingDTO::getDynamicAssetImport)
                .map(AssetImportDTO::getUsername)
                .orElse(null);

        if (Objects.isNull(username)) {
            log.warn("Vendor Username is null...");
            return null;
        }

        String vendorAssetId = userService.findOneByEmail(username)
                .map(User::getUserAssetId)
                .orElse(null);

        if (Objects.isNull(vendorAssetId)) {
            log.warn("Cannot find vendor with username {}", username);
        }
        return vendorAssetId;
    }

    public String fetchUpc(AssetImportDTO assetImportDTO) {
        String upc = assetImportDTO.getQuestion("productUpc");
        if (!Strings.isNullOrEmpty(upc)) {
            return upc;
        } else {
            return getUpcFromBarcode(assetImportDTO.getBarcodeItems())
                    .orElseGet(() -> {
                        log.warn("ScanId: {}, UPC not detected", assetImportDTO.getScanId());
                        return null;
                    });
        }
    }

    public String fetchOperatorAssetId(ImportWrappingDTO importWrappingDTO) {
        Long deviceId = Optional.ofNullable(importWrappingDTO)
                .map(ImportWrappingDTO::getDynamicAssetImport)
                .map(AssetImportDTO::getDeviceId).orElse(null);

        if (Objects.isNull(deviceId)) {
            log.warn("Operator Device id is null...");
            return null;
        }

        String operatorAssetId = userService.findFirstByCodereadrDeviceId(deviceId)
                .map(User::getUserAssetId)
                .orElse(null);

        if (Objects.isNull(operatorAssetId)) {
            log.warn("Cannot find operator with deviceId: {}", deviceId);
        }
        return operatorAssetId;
    }
}
