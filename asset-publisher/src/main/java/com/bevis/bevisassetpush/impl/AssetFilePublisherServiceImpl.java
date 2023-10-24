package com.bevis.bevisassetpush.impl;

import com.bevis.bevisassetpush.AssetFilePublisherService;
import com.bevis.bevisassetpush.TransactionDataGenerator;
import com.bevis.bevisassetpush.dto.FileParametersDTO;
import com.bevis.bevisassetpush.dto.FilePostDTO;
import com.bevis.bevisassetpush.dto.IpfsPostDTO;
import com.bevis.bevisassetpush.dto.TransactionDataDTO;
import com.bevis.blockchain.cryptopay.dto.Transaction;
import com.bevis.blockchain.filepush.BlockchainFilePushService;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.filecore.domain.File;
import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Master;
import com.bevis.filecore.domain.enumeration.FileState;
import com.bevis.filecore.FileService;
import com.bevis.files.util.FileUtil;
import com.bevis.ipfs.IpfsService;
import com.bevis.ipfs.dto.IpfsFile;
import com.bevis.master.MasterPermissionCheckingService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AssetFilePublisherServiceImpl implements AssetFilePublisherService {

    private final IpfsService ipfsService;
    private final TransactionDataGenerator transactionDataGenerator;
    private final BlockchainFilePushService blockchainFilePushService;
    private final MasterService masterService;
    private final FileService fileService;
    private final MasterPermissionCheckingService masterPermissionCheckingService;

    @Override
    public File publishFileToAsset(FilePostDTO filePostDTO) throws PermissionDeniedException {
        com.bevis.files.dto.File tempFile = filePostDTO.getTempFile();
        Master master = filePostDTO.getMaster();
        masterPermissionCheckingService.validateFilePublishPermission(master);
        final String blockchain = getBlockchain(master);
        File file = new File();
        file.setAssetId(master.getId());
        file.setBlockchain(blockchain);
        file.setSha256Hash(FileUtil.calculateSha256Hash(tempFile.getFile()));

        //Push file to IPFS
        final IpfsFile ipfsFile = ipfsService.postFileAndPin(tempFile);
        final String hash = ipfsFile.getHash();
        log.debug("File hash: {}", hash);
        file.setIpfs(ipfsFile.getHash());

        //Push file to Blockchain
        final String publicKey = master.getPublicKey();

        FileParametersDTO fileParametersDTO = filePostDTO.getFileParametersDTO();
        String transactionData = transactionDataGenerator.generateTransactionData(TransactionDataDTO.builder()
                .fileType(fileParametersDTO.getExtension())
                .ipfs(file.getIpfs())
                .build());
        log.debug("transactionData: {}", transactionData);

        final Transaction transaction = blockchainFilePushService.pushBevisDataToBlockchain(publicKey, blockchain, transactionData);
        file.setBlockchain(transaction.getBlockchain());
        file.setTransactionId(transaction.getTransactionId());
        file.setFileType(fileParametersDTO.getExtension());
        file.setFileSize(fileParametersDTO.getFileSizeInBytes());
        file.setFileName(fileParametersDTO.getFileName());
        file.setEncrypted(Objects.nonNull(filePostDTO.getEncrypted()) && filePostDTO.getEncrypted());

        return fileService.save(file);
    }

    @Override
    public File publishIpfsToAsset(IpfsPostDTO ipfsPostDTO) throws PermissionDeniedException {
        Master master = ipfsPostDTO.getMaster();
        masterPermissionCheckingService.validateFilePublishPermission(master);
        final String blockchain = getBlockchain(master);

        File file = new File();
        file.setAssetId(master.getId());
        file.setBlockchain(blockchain);
        file.setIpfs(ipfsPostDTO.getIpfs());

        //Push file to IPFS
        final boolean ipfsPublished = ipfsService.pinFile(ipfsPostDTO.getIpfs());
        log.debug("File hash: {} is published: {}", ipfsPostDTO.getIpfs(), ipfsPublished);

        //Push file to Blockchain
        final String publicKey = master.getPublicKey();

        String transactionData = transactionDataGenerator.generateTransactionData(TransactionDataDTO.builder()
                .fileType(ipfsPostDTO.getFileExtension())
                .ipfs(file.getIpfs())
                .build());
        log.debug("transactionData: {}", transactionData);

        final Transaction transaction = blockchainFilePushService.pushBevisDataToBlockchain(publicKey, blockchain, transactionData);
        file.setBlockchain(transaction.getBlockchain());
        file.setTransactionId(transaction.getTransactionId());
        file.setFileType(ipfsPostDTO.getFileExtension());
        file.setEncrypted(false);
        return fileService.save(file);
    }

    @Override
    public File reTryTransactionManually(File fileDTO) throws PermissionDeniedException {
        //Push file to Blockchain
        Master master = masterService.findById(fileDTO.getAssetId())
                .orElseThrow(ObjectNotFoundException::new);
        masterPermissionCheckingService.validateFilePublishPermission(master);
        if (!Objects.equals(fileDTO.getState(), FileState.REJECTED)) {
            log.debug("Error re-try. State must be rejected");
            throw new RuntimeException("Error re-try. State must be rejected");
        }
        final String publicKey = master.getPublicKey();
        final String blockchain = fileDTO.getBlockchain();

        String transactionData = transactionDataGenerator.generateTransactionData(TransactionDataDTO.builder()
                .fileType(fileDTO.getFileType().toUpperCase())
                .ipfs(fileDTO.getIpfs())
                .build());
        log.debug("transactionData: {}", transactionData);

        final Transaction transaction = blockchainFilePushService.pushBevisDataToBlockchain(publicKey, blockchain, transactionData);
        fileDTO.setBlockchain(transaction.getBlockchain());
        fileDTO.setTransactionId(transaction.getTransactionId());
        fileDTO.setState(FileState.UNCONFIRMED);
        fileDTO.setError(null);
        return fileService.save(fileDTO);
    }

    private String getBlockchain(Master master) {
        return Optional.ofNullable(master)
                .map(Master::getBlockchain)
                .map(Blockchain::getName)
                .orElse(null);
    }
}
