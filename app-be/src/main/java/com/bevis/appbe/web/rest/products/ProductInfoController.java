package com.bevis.appbe.web.rest.products;

import com.bevis.assetinfo.ProductInfoService;
import com.bevis.assetinfo.dto.ProductInfoDTO;
import com.bevis.assettype.AssetTypesService;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.assettype.domain.AssetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductInfoController {

    private final ProductInfoService productInfoService;
    private final AssetTypesService assetTypesService;

    @GetMapping("products/{assetId}")
    ProductInfoDTO getProduct(@PathVariable String assetId) {
        log.debug("getProduct by AssetId:{}", assetId);
        return productInfoService.getById(assetId);
    }

    @GetMapping("products/asset-types/{id}")
    AssetType getProductAssetType(@PathVariable Long id) {
        return assetTypesService.findById(id)
                .filter(AssetType::getIsProduct)
                .orElseThrow(ObjectNotFoundException::new);
    }

    @GetMapping("products/asset-types/")
    List<AssetType> getProductAssetTypes() {
        return assetTypesService.getProductAssetTypes();
    }
}
