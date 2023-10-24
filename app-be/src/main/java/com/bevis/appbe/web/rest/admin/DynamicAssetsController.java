package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.assettype.AssetTypesService;
import com.bevis.asset.DynamicAssetException;
import com.bevis.asset.DynamicAssetService;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.asset.dto.AssetRequest;
import com.bevis.master.dto.SearchMasterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.bevis.asset.FieldsUtil.getFields;
import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.VENDOR;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DynamicAssetsController {

    private final DynamicAssetService dynamicAssetService;
    private final AssetTypesService assetTypesService;

    @Secured({ADMIN, VENDOR})
    @GetMapping("admin/dynamic-assets/all")
    @ApiPageable
    DataResponse<AssetDTO> getAssets(SearchMasterRequest params,
                                     Pageable pageable) {
        log.debug("getAssets by params: {}", params);
        Page<AssetDTO> page = dynamicAssetService.findAll(params, pageable);
        return DataResponse.of(page, getFields(page.getContent()));
    }

    @Secured({ADMIN, VENDOR})
    @GetMapping("admin/dynamic-assets")
    AssetDTO getAsset(@RequestParam(value = "asset-id") String assetId) {
        log.debug("getAsset by AssetId:{}", assetId);
        return dynamicAssetService.getById(assetId);
    }

    @Secured(ADMIN)
    @PostMapping("admin/dynamic-assets")
    AssetDTO createAsset(@RequestBody @Valid AssetRequest assetRequest) throws DynamicAssetException {
        log.debug("Create dynamic asset : {}", assetRequest);
        return dynamicAssetService.create(assetRequest);
    }

    @Secured(ADMIN)
    @PutMapping("admin/dynamic-assets")
    AssetDTO updateAsset(@RequestBody @Valid AssetRequest assetRequest) throws DynamicAssetException {
        log.debug("Update dynamic asset : {}", assetRequest);
        return dynamicAssetService.update(assetRequest);
    }

    @Secured({ADMIN, VENDOR})
    @GetMapping("admin/dynamic-assets/fields")
    Map<String, Object> getAssetsFields(
            @RequestParam("asset-type-id") Long assetTypeId) {
        log.debug("getAssetsFields by AssetTypeId: {}", assetTypeId);
        return assetTypesService.getAssetsTypeFieldsByTypeId(assetTypeId);
    }

}
