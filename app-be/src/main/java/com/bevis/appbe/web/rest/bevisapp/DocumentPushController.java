package com.bevis.appbe.web.rest.bevisapp;

import com.bevis.appbe.service.DocumentParamsLoader;
import com.bevis.bevisassetpush.BevisAssetPushService;
import com.bevis.bevisassetpush.dto.AssetDocumentDTO;
import com.bevis.bevisassetpush.dto.BevisAssetDTO;
import com.bevis.asset.DynamicAssetException;
import com.bevis.master.PermissionDeniedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bevis.common.util.MapUtil.filterMap;
import static com.bevis.common.util.MapUtil.renameMapKey;
import static com.bevis.files.util.FileUtil.createEncryptedFile;
import static com.bevis.files.util.FileUtil.createTempFile;
import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.USER;

@Api(description = "Importing documents to IPFS & Blockchain")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class DocumentPushController {

    private final BevisAssetPushService bevisAssetPushService;
    private final DocumentParamsLoader documentParamsLoader;

    @Deprecated
    @ApiOperation(value = "Step0. Validating key if exists.")
    @Secured({ADMIN, USER})
    @GetMapping("master-documents/validate")
    BevisAssetDTO validateMasterExistence(@RequestParam("asset-id") String assetId) {
        log.debug("REST request to validate Master existence by assetId : {}", assetId);
        return bevisAssetPushService.checkAssetByAssetId(assetId);
    }

    @PostMapping("/v2/master-documents/")
    @Secured({ADMIN, USER})
    Map<String, String> uploadFileToNewAssetForUser(MultipartFile file,
                                                    String blockchain,
                                                    Boolean encrypted,
                                                    String password,
                                                    @RequestParam Map<String, Object> formData) throws DynamicAssetException, PermissionDeniedException {
        log.debug("uploadFileToNewAssetForUser");
        return bevisAssetPushService.uploadFileToNewAssetForUser(AssetDocumentDTO.builder()
//                .file(createTempFile(file))
                .file(encrypted && Objects.nonNull(password) ? createEncryptedFile(file, password) : createTempFile(file))
                .blockchain(blockchain)
                .encrypted(encrypted)
                .dynamicData(prepareDynamicData(formData))
                .fileParameters(documentParamsLoader.getFileParametersFromDocument(file))
                .build());
    }

    @PutMapping("/v2/master-documents/")
    @Secured({ADMIN, USER})
    Map<String, String> uploadFileToExistedAsset(MultipartFile file,
                                                 String assetId,
                                                 Boolean encrypted,
                                                 String password,
                                                 @RequestParam Map<String, Object> formData) throws DynamicAssetException, PermissionDeniedException {
        log.debug("uploadFileToExistedAsset");
        return bevisAssetPushService.uploadFileToExistedAssetForUser(AssetDocumentDTO.builder()
                .file(encrypted && Objects.nonNull(password) ? createEncryptedFile(file, password) : createTempFile(file))
                .assetId(assetId)
                .encrypted(encrypted)
                .dynamicData(prepareDynamicData(formData))
                .fileParameters(documentParamsLoader.getFileParametersFromDocument(file))
                .build());
    }

    @NotNull
    private Map<String, Object> prepareDynamicData(Map<String, Object> formData) {
        Map<String, Object> dynamicData = filterMap(formData, getStaticParams());
        if (dynamicData.containsKey("brand")) {
            renameMapKey(dynamicData, "brand", "phone_brand");
        }
        if (dynamicData.containsKey("model")) {
            renameMapKey(dynamicData, "model", "phone_model");
        }
        return dynamicData;
    }

    @NotNull
    private List<String> getStaticParams() {
        return Arrays.asList("blockchain", "encrypted", "assetId", "password");
    }
}
