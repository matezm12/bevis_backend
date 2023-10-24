package com.bevis.bevisassetpush.impl;

import com.bevis.bevisassetpush.BevisAssetPushProperties;
import com.bevis.bevisassetpush.DocumentFileValidator;
import com.bevis.bevisassetpush.dto.FileParametersDTO;
import com.bevis.bevisassetpush.exception.BevisAssetPushException;
import com.bevis.filecode.FileCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
class DocumentFileValidatorImpl implements DocumentFileValidator {

    private final static Long MEGA_BYTE = 1048576L;

    private final BevisAssetPushProperties bevisAssetPushProperties;
    private final FileCodeService fileCodeService;

    @Override
    public void validateFile(FileParametersDTO fileParametersDTO) {
        final String extension = Strings.toRootUpperCase(fileParametersDTO.getExtension());
        final long fileSizeInBytes = fileParametersDTO.getFileSizeInBytes();
        final Long maxFileSizeInBytes = bevisAssetPushProperties.getMaxFileSizeInBytes();
        if (fileSizeInBytes > maxFileSizeInBytes) {
            BigDecimal fileSizeInMegabytes = BigDecimal.valueOf(fileSizeInBytes / (double) MEGA_BYTE)
                    .setScale(2, RoundingMode.DOWN);
            BigDecimal maxFileSizeInMegabytes = BigDecimal.valueOf(maxFileSizeInBytes / (double) MEGA_BYTE)
                    .setScale(2, RoundingMode.DOWN);
            throw new BevisAssetPushException("File too big: " + fileSizeInMegabytes +
                    " MB. Max file-size is " + maxFileSizeInMegabytes + " MB");
        }
        Set<String> supportedExtensions = fileCodeService.getSupportedExtensions();
        if (!supportedExtensions.contains(extension)) {
            throw new BevisAssetPushException("Document File format: " + extension + " does not supported ");
        }
    }
}
