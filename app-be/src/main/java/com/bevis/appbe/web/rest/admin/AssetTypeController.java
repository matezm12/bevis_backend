package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.assettype.AssetTypesMgmtService;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.assettype.domain.AssetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.bevis.security.AuthoritiesConstants.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssetTypeController {

    private final AssetTypesMgmtService assetTypesMgmtService;

    @Secured(ADMIN)
    @GetMapping("admin/asset-types/{id}")
    AssetType findOne(@PathVariable Long id){
        log.debug("REST to load asset type {}...", id);
        return assetTypesMgmtService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Asset type with id " + id + " not found "));
    }

    @Secured({ADMIN, VENDOR})
    @ApiPageable
    @GetMapping("admin/asset-types")
    DataResponse<AssetType> getAllAssetTypes(String search, Pageable pageable) {
        log.debug("REST to load asset types...");
        return DataResponse.of(assetTypesMgmtService.searchAll(search, pageable));
    }

    @Secured(ADMIN)
    @PostMapping("admin/asset-types")
    AssetType create(@RequestBody @Valid AssetType assetType){
        log.debug("REST to create assetType: {}", assetType);
        return assetTypesMgmtService.save(assetType);
    }

    @Secured(ADMIN)
    @PutMapping("admin/asset-types")
    AssetType update(@RequestBody @Valid AssetType assetType){
        log.debug("REST to update assetType: {}", assetType);
        if (Objects.isNull(assetType.getId())){
            throw new RuntimeException("Id is null");
        }
        return assetTypesMgmtService.save(assetType);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/asset-types/{id}")
    void delete(@PathVariable Long id){
        log.debug("REST to delete assetType by ID: {}", id);
        assetTypesMgmtService.deleteById(id);
    }
}
