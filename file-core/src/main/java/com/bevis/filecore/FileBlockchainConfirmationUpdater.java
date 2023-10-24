package com.bevis.filecore;

import com.bevis.blockchain.blockchain.BlockchainGatewayService;
import com.bevis.blockchain.blockchain.dto.TransactionStatus;
import com.bevis.blockchain.blockchain.exception.TxNotFoundBlockchainException;
import com.bevis.common.async.AsyncService;
import com.bevis.filecore.domain.File;
import com.bevis.filecore.domain.enumeration.FileState;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class FileBlockchainConfirmationUpdater {

    private final BlockchainGatewayService blockchainGatewayService;
    private final FileService fileService;
    private final FileEventListener fileEventListener;
    private final AsyncService asyncService;

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 2 * 60 * 1000)
    void updateUnconfirmedFilesState(){
        final List<File> allUnconfirmed = fileService.findAllUnconfirmed().stream()
                .filter(x->!Strings.isNullOrEmpty(x.getBlockchain()))
                .filter(x->!Objects.equals(x.getState(), FileState.IGNORED))
                .filter(x->!Objects.equals(x.getState(), FileState.REJECTED))
                .collect(Collectors.toList());
        for (File file: allUnconfirmed){
            try {
                final String blockchain = file.getBlockchain();
                final Long id = file.getId();
                final String transactionId = file.getTransactionId();
                if (Objects.nonNull(transactionId)) {
                    log.debug("Checking transaction status of file ID: {}, trans ID: {}, blockchain: {}", id, transactionId, blockchain);
                    final TransactionStatus transactionStatus = blockchainGatewayService.getTransactionStatus(blockchain, transactionId);
                    if (transactionStatus.getConfirmationsCount() > 0) {
                        updateBlock(file.getId(), transactionStatus.getTransactionBlock());
                        log.info("File ID: {}, transaction ID: {} status changed to CONFIRMED, blockchain: {}", id, transactionId, blockchain);
                    } else {
                        log.warn("File ID: {}, transaction ID: {} status NOT changed, blockchain: {}", id, transactionId, blockchain);
                    }
                }
            } catch (TxNotFoundBlockchainException e) {
                log.warn("Blockchain not found for tx:{}, blockchain:{}", file.getTransactionId(), file.getBlockchain());
            } catch (Exception e){
                log.error("Error updating file block: {}", e.getMessage());
                file.setState(FileState.REJECTED);
                file.setError(e.getMessage());
                fileService.save(file);
            }

        }
    }

    private void updateBlock(Long id, Long transactionBlock) {
        asyncService.run(() -> fileService.findById(id).ifPresent(file -> {
            file.setBlock(transactionBlock);
            file.setState(FileState.CONFIRMED);
            fileService.save(file);
            fileEventListener.onFileBlockUpdated(file);
        }));
    }
}
