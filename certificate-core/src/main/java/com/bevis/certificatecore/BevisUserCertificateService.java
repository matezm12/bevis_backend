package com.bevis.certificatecore;

import com.bevis.assettype.AssetTypesService;
import com.bevis.bevisassetpush.AssetFilePublisherService;
import com.bevis.bevisassetpush.dto.FileParametersDTO;
import com.bevis.bevisassetpush.dto.FilePostDTO;
import com.bevis.blockchain.blockchain.BlockchainGatewayService;
import com.bevis.blockchain.datasign.DataSignService;
import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchainfile.TransactionUtil;
import com.bevis.filecore.domain.File;
import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;
import com.bevis.asset.DynamicAssetService;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.filecore.FileEventHandler;
import com.bevis.filecore.FileService;
import com.bevis.gateway.UrlGatewayService;
import com.bevis.master.MasterPermissionCheckingService;
import com.bevis.master.MasterService;
import com.bevis.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
class BevisUserCertificateService implements FileEventHandler {

    private final CertificateFileBuilder certificateFileBuilder;
    private final EmailCertAttachmentBuilder emailCertAttachmentBuilder;
    private final CertificateSender certificateSender;

    private final MasterService masterService;
    private final BlockchainGatewayService blockchainGatewayService;
    private final UrlGatewayService urlGatewayService;
    private final BlockchainUrlGateway blockchainUrlGateway;
    private final MasterPermissionCheckingService masterPermissionCheckingService;

    private final DataSignService dataSignService;
    private final DynamicAssetService dynamicAssetService;
    private final AssetFilePublisherService assetFilePublisherService;
    private final CertificateDataMapper certificateDataMapper;
    private final AssetTypesService assetTypesService;
    private final FileService fileService;
    private final UserService userService;

    @Transactional
    @Override
    public void onFileBlockUpdated(Long fileId) {
        try {
            File file = fileService.findById(fileId).orElse(null);
            if (Objects.isNull(file)) {
                return;
            }
            String assetId = file.getAssetId();
            log.debug("Trying to generate certificate for asset: {}, file ID: {}", assetId, fileId);

            Master master = masterService.findById(assetId).orElse(null);
            if (Objects.isNull(master)) {
                log.warn("Cannot find master...");
                return;
            }
            if (Objects.equals(master.getAssetType(), assetTypesService.getBevisAssetType()) && Objects.isNull(master.getCertificate()))  {
                log.debug("tryToGenerateCertificateForAssetOfFile");
                tryToGenerateCertificateForAssetOfFile(master, file);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void tryToGenerateCertificateForAssetOfFile(Master master, File file) throws PermissionDeniedException {

        if (Objects.nonNull(master.getCertificate())) {
            log.warn("Certificate already generated for asset: {}", master.getId());
            return;
        }

        String assetId = file.getAssetId();
        String blockchain = master.getBlockchain().getName();

        masterPermissionCheckingService.validateCertificatePublishingPermission(master);
        AssetDTO asset = dynamicAssetService.getById(assetId);

        Map<String, String> certificateData = certificateDataMapper.constructCertificateData(master, asset, file);

        final java.io.File certificateFile = certificateFileBuilder.build(certificateData);
        java.io.File attachment = null;
        try {
            attachment = emailCertAttachmentBuilder.build(certificateData);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        final String existedCertTransactionId = null; //tryToLoadExistedCertificateTransaction(publicKey, blockchain);
        if (Objects.nonNull(existedCertTransactionId)) {
            log.warn("Certificate transaction was creating before. Check possible errors.");
            return;
        }
        com.bevis.files.dto.File certificateFileDto = com.bevis.files.dto.File.builder()
                .fileName(certificateFile.getName())
                .file(certificateFile)
                .build();

        User assetCreatorUser = userService.findByAssetId(master.getCreatorAssetId()).orElse(null);
        if (assetCreatorUser == null) {
            log.warn("Cannot find user of asset: {}", master.getId());
        }
        final File certificate = assetFilePublisherService.publishFileToAsset(FilePostDTO.builder()
                .tempFile(certificateFileDto)
                .master(master)
                .currentUser(assetCreatorUser)
                .fileParametersDTO(new FileParametersDTO(getFileSize(certificateFile), "htm", certificateFile.getName()))
                .encrypted(false)
                .build());
        master.setCertificate(certificate);
        masterService.saveMaster(master);

        certificateData.put("certificateIpfsCid", certificate.getIpfs());
        certificateData.put("certificateIpfsCidLink", urlGatewayService.getIpfsUrl(certificate.getIpfs()));
        certificateData.put("certificateTransactionId", certificate.getTransactionId());
        certificateData.put("block", Objects.nonNull(file.getBlock()) ? file.getBlock().toString(): "none");

        String txLink = blockchainUrlGateway.getBlockchainTransactionLink(file.getTransactionId(), master.getBlockchain());
        certificateData.put("certificateTransactionIdLink", txLink);

        if (Objects.nonNull(assetCreatorUser)) {
            log.debug("Sending email with certificate.");
            certificateSender.send(assetCreatorUser.getEmail(), certificateData, attachment);
        }
    }

    private long getFileSize(java.io.File file) {
        if (Objects.isNull(file)) {
            return 0L;
        }
        try {
            return Files.size(file.toPath());
        } catch (IOException e) {
            return 0L;
        }
    }

    private String tryToLoadExistedCertificateTransaction(String publicKey, String blockchain) {
        return blockchainGatewayService.loadTransactionsByAddress(blockchain, publicKey).stream()
                .filter(x -> validateTransaction(x, publicKey))
                .sorted(TransactionUtil::compareTx)
                .filter(TransactionUtil::isCertificateTx)
                .findFirst()
                .map(com.bevis.blockchain.blockchain.dto.Transaction::getTransactionId)
                .orElse(null);
    }

    private boolean validateTransaction(com.bevis.blockchain.blockchain.dto.Transaction x, String address) {
        return Objects.nonNull(x.getData()) && dataSignService.validateSignedData(x.getData(), address);
    }

}
