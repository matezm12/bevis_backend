package com.bevis.bevisassetpush.impl;

import com.bevis.bevisassetpush.TransactionDataGenerator;
import com.bevis.bevisassetpush.dto.TransactionDataDTO;
import com.bevis.filecode.FileCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TransactionDataGeneratorImpl implements TransactionDataGenerator {

    private final FileCodeService fileCodeService;

    @Override
    public String generateTransactionData(TransactionDataDTO transactionDataDTO) {
        String fileCode = fileCodeService.getCodeByFileType(transactionDataDTO.getFileType());
        return fileCode + "." + transactionDataDTO.getIpfs();
    }
}
