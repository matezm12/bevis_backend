package com.bevis.certificatecore.impl;

import com.bevis.certificatecore.CertificatePublishingService;
import com.bevis.certificatecore.dto.CertPublishDTO;
import com.bevis.blockchain.cryptopay.dto.Transaction;
import com.bevis.blockchain.filepush.BlockchainFilePushService;
import com.bevis.certificate.CertificateService;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.master.domain.Master;
import com.bevis.filecore.repository.FileRepository;
import com.bevis.master.repository.MasterRepository;
import com.bevis.ipfs.IpfsService;
import com.bevis.ipfs.dto.IpfsFile;
import com.bevis.master.MasterPermissionCheckingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import static com.bevis.files.util.FileUtil.calculateSha256Hash;
import static java.io.File.createTempFile;

@Service
@Slf4j
@RequiredArgsConstructor
class CertificatePublishingServiceImpl implements CertificatePublishingService {

    private final IpfsService ipfsService;
    private final MasterRepository masterRepository;
    private final BlockchainFilePushService blockchainFilePushService;
    private final FileRepository fileRepository;
    private final CertificateService certificateService;
    private final MasterPermissionCheckingService masterPermissionCheckingService;

    @Transactional
    @Override
    public void publish(CertPublishDTO certPublishDTO) throws Exception {
        try {
            String assetId = certPublishDTO.getAssetId();
            Master master = masterRepository.findById(assetId)
                    .orElseThrow(ObjectNotFoundException::new);
            masterPermissionCheckingService.validateCertificatePublishingPermission(master);
            File certificateFile = createTempFile(UUID.randomUUID().toString(), "");
            certificateFile.deleteOnExit();

            try (FileOutputStream fileOutputStream = new FileOutputStream(certificateFile)) {
                certificateService.constructCertificateForAsset(assetId, fileOutputStream);
            }

            com.bevis.files.dto.File certificateFileDto = com.bevis.files.dto.File.builder()
                    .fileName(certificateFile.getName())
                    .file(certificateFile)
                    .build();

            final IpfsFile certIpfsFile = ipfsService.postFileAndPin(certificateFileDto);
            final String certFileHash = certIpfsFile.getHash();

            final String publicKey = master.getPublicKey();
            Blockchain blockchain = master.getBlockchain();
            String blockchainName = blockchain.getName();
            final Transaction transaction = blockchainFilePushService.pushBevisCertToBlockchain(publicKey, blockchainName, certFileHash);

            com.bevis.filecore.domain.File fileCertificate = new com.bevis.filecore.domain.File();
            fileCertificate.setAssetId(master.getId());
            fileCertificate.setBlockchain(blockchainName);
            fileCertificate.setSha256Hash(calculateSha256Hash(certificateFile));
            fileCertificate.setTransactionId(transaction.getTransactionId());
            fileCertificate.setIpfs(certFileHash);
            fileCertificate.setFileType("htm");
            fileCertificate.setEncrypted(false);
            fileRepository.saveAndFlush(fileCertificate);
            master.setCertificate(fileCertificate);
            masterRepository.save(master);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
