package com.bevis.bevisassetpush.impl;

import com.bevis.assettype.AssetTypesService;
import com.bevis.assettype.domain.AssetType;
import com.bevis.bevisassetpush.AssetFilePublisherService;
import com.bevis.bevisassetpush.BevisAssetPushService;
import com.bevis.bevisassetpush.DocumentFileValidator;
import com.bevis.bevisassetpush.dto.*;
import com.bevis.bevisassetpush.mapper.AssetMapper;
import com.bevis.bevisassetpush.mapper.FileUploadResponseMapper;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.asset.DynamicAssetException;
import com.bevis.asset.DynamicAssetService;
import com.bevis.asset.FieldsUtil;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.asset.dto.AssetRequest;
import com.bevis.filecore.domain.File;
import com.bevis.lister.ListerAssetService;
import com.bevis.lister.dto.ListerAssetRequest;
import com.bevis.master.MasterImportService;
import com.bevis.master.MasterService;
import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Master;
import com.bevis.user.UserService;
import com.bevis.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.bevis.bevisassetpush.SecurityValidationUtil.validateMasterOwnership;
import static com.bevis.common.util.MapUtil.toUnderscoreMapKeys;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class BevisAssetPushServiceImpl implements BevisAssetPushService {

    private final AssetFilePublisherService assetFilePublisherService;
    private final DocumentFileValidator documentFileValidator;
    private final MasterService masterService;
    private final AssetMapper assetMapper;
    private final MasterImportService masterImportService;
    private final ListerAssetService listerAssetService;
    private final DynamicAssetService dynamicAssetService;
    private final FileUploadResponseMapper fileUploadResponseMapper;
    private final UserService userService;
    private final AssetTypesService assetTypesService;

    @Deprecated
    @Override
    @Transactional(readOnly = true)
    public BevisAssetDTO checkAssetByAssetId(String assetId) {
        Master master = getMaster(assetId);
        validateMasterOwnership(master, userService.getCurrentUser());
        return assetMapper.toDto(master);
    }

    @Transactional
    @Override
    public Map<String, String> uploadFileToNewAssetForUser(AssetDocumentDTO document) throws DynamicAssetException, PermissionDeniedException {
        Master master = generateMaster(document.getBlockchain());
        master.setAssetType(assetTypesService.getBevisAssetType());
        master.setOwnerAssetId(userService.getCurrentUser().getUserAssetId());
        return pushFileToBevisAsset(document, master);
    }

    @Transactional
    @Override
    public Map<String, String> uploadFileToExistedAssetForUser(AssetDocumentDTO document) throws DynamicAssetException, PermissionDeniedException {
        Master master = getMaster(document.getAssetId());
        if (!Objects.equals(master.getAssetType(), assetTypesService.getBevisAssetType())) {
            master.setAssetType(assetTypesService.getBevisAssetType());
        }
        User currentUser = userService.getCurrentUser();
        if (Objects.isNull(master.getOwnerAssetId())) {
            master.setOwnerAssetId(currentUser.getUserAssetId());
        }
        validateMasterOwnership(master, currentUser);
        return pushFileToBevisAsset(document, master);
    }

    @Override
    public File publishFileToAsset(String assetId, PublishedFileDTO publishedFileDTO) throws PermissionDeniedException {
        log.debug("publishFileToAsset...");
        Master master = getMaster(assetId);
        User currentUser = userService.getCurrentUser();
        if (Objects.isNull(master.getOwnerAssetId())) {
            master.setOwnerAssetId(currentUser.getUserAssetId());
        }
        validateMasterOwnership(master, currentUser);
        log.debug("File successfully validated.");
        final File savedFileDTO = assetFilePublisherService.publishFileToAsset(FilePostDTO.builder()
                .tempFile(publishedFileDTO.getFile())
                .master(master)
                .currentUser(publishedFileDTO.getUser())
                .fileParametersDTO(publishedFileDTO.getParams())
                .encrypted(publishedFileDTO.getEncrypted())
                .build());
        savedFileDTO.setMetadata(publishedFileDTO.getMetadata());
        if (Objects.nonNull(publishedFileDTO.getCreditsPayment())) {
            savedFileDTO.setCreditsPaymentId(publishedFileDTO.getCreditsPayment().getId());
        }
        if (Objects.isNull(master.getFile())) {
            master.setFile(savedFileDTO);
            masterService.update(master);
        }
        log.info("File successfully pushed to IPFS & Blockchain: {}", savedFileDTO.getId());
        return savedFileDTO;
    }

    @Override
    public File publishIpfsToAsset(String assetId, PublishedIpfsDTO publishedIpfsDTO) throws PermissionDeniedException {
        log.debug("publishIpfsToAsset...");
        Master master = getMaster(assetId);
        User currentUser = userService.getCurrentUser();
        if (Objects.isNull(master.getOwnerAssetId())) {
            master.setOwnerAssetId(currentUser.getUserAssetId());
        }
        validateMasterOwnership(master, currentUser);
        log.debug("File successfully validated.");
        final File savedFileDTO = assetFilePublisherService.publishIpfsToAsset(IpfsPostDTO.builder()
                .ipfs(publishedIpfsDTO.getIpfs())
                .master(master)
                .currentUser(publishedIpfsDTO.getUser())
                .fileExtension(publishedIpfsDTO.getFileExtension())
                .build());
        if (Objects.isNull(master.getFile())) {
            master.setFile(savedFileDTO);
            masterService.update(master);
        }
        log.info("File successfully pushed to IPFS & Blockchain: {}", savedFileDTO.getId());
        return savedFileDTO;
    }

    @NotNull
    private Map<String, String> pushFileToBevisAsset(AssetDocumentDTO document, Master master) throws DynamicAssetException, PermissionDeniedException {
        FileParametersDTO fileParametersDTO = document.getFileParameters();
        documentFileValidator.validateFile(fileParametersDTO);
        final String DOCUMENT_FORMAT = "document_format";
        final String FILE_SIZE = "file_size";

        Map<String, Object> dynamicData = toUnderscoreMapKeys(new HashMap<>(document.getDynamicData()));
        dynamicData.put(DOCUMENT_FORMAT, fileParametersDTO.getExtension());
        dynamicData.put(FILE_SIZE, fileParametersDTO.getFileSizeInBytes());

        String assetId = master.getId();

        AssetDTO asset;
        if (Objects.isNull(document.getAssetId())) {
            asset = dynamicAssetService.create(AssetRequest.builder()
                    .assetId(assetId)
                    .fieldValues(FieldsUtil.mapFieldValues(dynamicData))
                    .assetTypeId(Optional.ofNullable(master.getAssetType()).map(AssetType::getId).orElse(null))
                    .build());
        } else {
            asset = dynamicAssetService.getById(assetId);
        }

        User user = userService.getCurrentUser();
        File file = this.publishFileToAsset(assetId, PublishedFileDTO.builder()
                .file(document.getFile())
                .user(user)
                .encrypted(document.getEncrypted())
                .params(fileParametersDTO)
                .metadata(dynamicData)
                .build());

        listerAssetService.addToUserList(new ListerAssetRequest(assetId, null), user);

        return fileUploadResponseMapper.mapAsset(master, asset, file);
    }

    private Master generateMaster(String blockchain) {
        log.debug("Generating new master record for NEW asset");
        Master master = masterImportService.generateBevisMasterByBlockchain(blockchain);
        masterService.update(master);
        return master;
    }

    private Master getMaster(String assetId) {
        return masterService.findByIdOrPublicKey(assetId)
                .orElseThrow(() -> new ObjectNotFoundException("Master with assetId: " + assetId + " not exist"));
    }

}
