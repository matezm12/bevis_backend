package com.bevis.appbe.web.rest.admin;


import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.bevisassetpush.BevisAssetPushService;
import com.bevis.bevisassetpush.dto.IpfsDTO;
import com.bevis.bevisassetpush.dto.PublishedFileDTO;
import com.bevis.bevisassetpush.dto.PublishedIpfsDTO;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Master;
import com.bevis.asset.AssetsRelationService;
import com.bevis.master.MasterImportService;
import com.bevis.master.MasterService;
import com.bevis.master.dto.MasterPartUpdate;
import com.bevis.master.dto.SearchMasterRequest;
import com.bevis.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.bevis.appbe.web.rest.util.MultipartFileUtil.getParametersFromMultipartFile;
import static com.bevis.files.util.FileUtil.createEncryptedFile;
import static com.bevis.files.util.FileUtil.createTempFile;
import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.VENDOR;
import static com.bevis.security.util.SecurityUtils.checkPermissions;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MasterController {

    private final BevisAssetPushService bevisAssetPushService;
    private final UserService userService;
    private final MasterService masterService;
    private final AssetsRelationService assetsRelationService;
    private final MasterImportService masterImportService;

    @Secured(ADMIN)
    @GetMapping("admin/masters/search-one")
    Master searchOne(SearchMasterRequest searchMasterRequest) {
        log.debug("REST request to search Master: {}", searchMasterRequest);
        return masterService.findOne(searchMasterRequest)
                .orElseThrow(() -> new ObjectNotFoundException("Master with search: [" + searchMasterRequest + "] not found "));
    }

    @Secured({ ADMIN, VENDOR })
    @GetMapping("admin/masters/{assetId}")
    Master findOne(@PathVariable String assetId) {
        log.debug("REST request to get Master: {}", assetId);
        return masterService.findByIdOrPublicKey(assetId)
                .map(x -> checkPermissions(x, x.getOwnerAssetId()))
                .orElseThrow(() -> new ObjectNotFoundException("Master with assetId " + assetId + " not found "));
    }

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/masters")
    DataResponse<Master> findAll(SearchMasterRequest searchMasterRequest,
                                 @NotNull Pageable pageable) {
        log.debug("REST request to get all Masters");
        return DataResponse.of(masterService.searchMaster(searchMasterRequest, pageable));
    }

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/masters/list")
    List<Master> findAllList(SearchMasterRequest searchMasterRequest) {
        log.debug("REST request to get all Masters list: by filter");
        return masterService.searchMaster(searchMasterRequest);
    }

    @Secured(ADMIN)
    @PostMapping("admin/masters/generate")
    Master generateMaster(@RequestParam("blockchain-name") String blockchainName) {
        log.debug("REST request to generate new Master");
        Master master = masterImportService.generateMasterByBlockchain(blockchainName, null);
        return masterService.saveMaster(master);
    }

    @Secured(ADMIN)
    @PostMapping("admin/masters")
    Master createMaster(@RequestBody Master master) {
        log.debug("REST request to save Master : {}", master);
        return masterService.saveMaster(master);
    }

    @Secured({ADMIN, VENDOR})
    @PutMapping("admin/masters")
    Master updateMaster(@RequestBody @Valid MasterPartUpdate partUpdate) {
        log.debug("REST request to update Master : {}", partUpdate);
        return masterService.updateMaster(partUpdate);
    }

    @Secured({ADMIN, VENDOR})
    @DeleteMapping("admin/masters/{assetId}")
    void deleteMaster(@PathVariable String assetId) {
        log.debug("REST request to delete Master : {}", assetId);
        masterService.deleteById(assetId);
    }

    @Secured({ADMIN, VENDOR})
    @PostMapping("admin/masters/{assetId}/file")
    void publishFileToAsset(@PathVariable String assetId, MultipartFile file, String password) throws PermissionDeniedException {
        log.debug("REST request to publish new file to asset with ID: {}", assetId);
        boolean encrypted = Strings.isNotEmpty(password);
        bevisAssetPushService.publishFileToAsset(
                assetId,
                PublishedFileDTO.builder()
                        .file(encrypted ? createEncryptedFile(file, password) : createTempFile(file))
                        .user(userService.getCurrentUser())
                        .params(getParametersFromMultipartFile(file, encrypted))
                        .encrypted(encrypted)
                        .build());
        log.info("File published to IPFS & Blockchain for asset: {}", assetId);
    }

    @Secured({ADMIN, VENDOR})
    @PostMapping("admin/masters/{assetId}/ipfs")
    void publishIpfsToAsset(@PathVariable String assetId, @RequestBody @Valid IpfsDTO body) throws PermissionDeniedException {
        log.debug("REST request to publish new ipfs to asset with ID: {}", assetId);
        bevisAssetPushService.publishIpfsToAsset(
                assetId,
                PublishedIpfsDTO.builder()
                        .ipfs(body.getIpfs())
                        .user(userService.getCurrentUser())
                        .fileExtension(body.getFileExtension())
                        .build());
        log.info("File published to IPFS & Blockchain for asset: {}", assetId);
    }

    @Secured({ADMIN, VENDOR})
    @GetMapping("admin/masters/{assetId}/children")
    DataResponse<Master> findMasterChildren(@PathVariable String assetId,
                                            Pageable pageable) {
        log.debug("REST request to get children if Master ID:{}", assetId);
        return DataResponse.of(masterService.findByIdOrPublicKey(assetId)
                .map(Master::getId)
                .map(id -> assetsRelationService.findChildrenOfAssetId(id, pageable))
                .orElse(new PageImpl<>(new ArrayList<>())));
    }

    @Secured({ADMIN, VENDOR})
    @GetMapping("admin/masters/{assetId}/parents")
    DataResponse<Master> findMasterParents(@PathVariable String assetId,
                                           Pageable pageable) {
        log.debug("REST request to get children if Master ID:{}", assetId);
        return DataResponse.of(masterService.findByIdOrPublicKey(assetId)
                .map(Master::getId)
                .map(id -> assetsRelationService.findParentsOfAssetId(id, pageable))
                .orElse(new PageImpl<>(new ArrayList<>())));
    }

    @Secured({ADMIN})
    @PutMapping("admin/masters/{assetId}/deactivate")
    Master deactivateMaster(@PathVariable String assetId) {
        log.debug("REST request to deactivate Master ID:{}", assetId);
        return masterService.deactivateMaster(assetId);
    }
}
