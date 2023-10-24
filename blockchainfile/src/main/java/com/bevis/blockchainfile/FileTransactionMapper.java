package com.bevis.blockchainfile;

import com.bevis.blockchain.blockchain.dto.Transaction;
import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchainfile.dto.FileDTO;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.filecore.domain.File;
import com.bevis.filecore.repository.FileRepository;
import com.bevis.gateway.UrlGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class FileTransactionMapper {

    private static final String UNNAMED = "Unnamed";

    private final FileRepository fileRepository;//todo remove form mapper
    private final BlockchainUrlGateway blockchainUrlGateway;
    private final UrlGatewayService urlGatewayService;

    FileDTO map(Transaction transaction, Blockchain blockchain){
        String[] transactionData = transaction.getData().split("\\.");
        String ipfsHash = transactionData[2];
        Optional<File> fileOpt = fileRepository.findFirstByIpfs(ipfsHash);
        String transactionId = transaction.getTransactionId();
        return FileDTO.builder()
                .id(fileOpt.map(File::getId).orElse(null))
                .fileName(fileOpt.map(File::getFileName).orElse(UNNAMED))
                .transactionId(transactionId)
                .ipfsHash(ipfsHash)
                .url(urlGatewayService.getIpfsUrl(ipfsHash))
                .txUrl(blockchainUrlGateway.getBlockchainTransactionLink(transactionId, blockchain))
                .fileType(fileOpt
                        .map(File::getFileType)
                        .map(String::toUpperCase).orElse(null))
                .encrypted(fileOpt.map(File::getEncrypted).orElse(false))
                .build();
    }
}
