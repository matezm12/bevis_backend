package com.bevis.appbe.web.rest.admin;

import com.bevis.assetinfo.AdminAssetGroupsInfoService;
import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.VENDOR;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminAssetInfoController {

    private final AdminAssetGroupsInfoService adminAssetGroupsInfoService;

    @GetMapping("/admin/v2/asset-info")
    @Secured({ADMIN, VENDOR})
    AssetGroupsInfoWrapper getFullAssetInfo(@RequestParam("asset-id") String assetId) {
        log.debug("REST request to get Asset by id : {}", assetId);
        return adminAssetGroupsInfoService.getAssetInfo(assetId);
    }
}
