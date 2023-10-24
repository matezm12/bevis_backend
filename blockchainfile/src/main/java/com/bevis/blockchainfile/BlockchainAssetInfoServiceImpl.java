package com.bevis.blockchainfile;

import com.bevis.blockchainfile.dto.AssetFilesDTO;
import com.bevis.blockchainfile.dto.FileDTO;
import com.bevis.blockchain.blockchain.BlockchainGatewayService;
import com.bevis.blockchain.blockchain.dto.Transaction;
import com.bevis.blockchain.datasign.DataSignService;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.master.domain.Master;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class BlockchainAssetInfoServiceImpl implements BlockchainAssetInfoService {

    private static final String BEVIS_FILE_TRANSACTION_PREFIX = "Bevis.";

    private final MasterService masterService;

    private final BlockchainGatewayService blockchainGatewayService;
    private final FileTransactionMapper fileTransactionMapper;
    private final DataSignService dataSignService;

    @Override
    public AssetFilesDTO getFilesByAssetIdOrPublicKey(String assetIdOrPublicKey) {
        Master master = masterService.findFirstByPublicKeyOrId(assetIdOrPublicKey, assetIdOrPublicKey)
                .orElseThrow(() -> new ObjectNotFoundException("Master with assetId (public key) " + assetIdOrPublicKey + " not found"));
        return loadAssetFilesByMaster(master);
    }

    private AssetFilesDTO loadAssetFilesByMaster(Master master) {
        String publicKey = master.getPublicKey();
        final String blockchain = master.getBlockchain().getName().toUpperCase();
        List<Transaction> allTransactions = blockchainGatewayService.loadTransactionsByAddress(blockchain, publicKey).stream()
                .filter(x -> validateTransaction(x, publicKey))
                .sorted(TransactionUtil::compareTx)
                .collect(Collectors.toList());

        final FileDTO certificate = allTransactions.stream()
                .filter(TransactionUtil::isCertificateTx)
                .map((Transaction transaction) -> fileTransactionMapper.map(transaction, master.getBlockchain()))
                .findFirst()
                .orElse(null);
        final List<FileDTO> files = allTransactions.stream()
                .filter(TransactionUtil::isFileTx)
                .map((Transaction transaction) -> fileTransactionMapper.map(transaction, master.getBlockchain()))
                .collect(Collectors.toList());
        return new AssetFilesDTO(certificate, files);
    }

    private boolean validateTransaction(Transaction x, String address) {
        return Objects.nonNull(x.getData())
                && x.getData().startsWith(BEVIS_FILE_TRANSACTION_PREFIX)
                && dataSignService.validateSignedData(x.getData(), address);
    }
}
