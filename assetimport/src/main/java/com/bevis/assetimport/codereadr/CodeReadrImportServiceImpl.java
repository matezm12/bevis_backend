package com.bevis.assetimport.codereadr;

import com.bevis.assetimport.AssetImportService;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.CodeReadrMessage;
import com.bevis.assetimport.dto.CodeReadrResponse;
import com.bevis.common.exception.PermissionDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class CodeReadrImportServiceImpl implements CodeReadrImportService {

    @Value("${codereadr.api-key}")
    private String apiKey;

    private final AssetImportService assetImportService;
    private final CodeReadrImportMapper codeReadrImportMapper;

    @Override
    public CodeReadrResponse importAssetsFromCodereadrBody(String codeReadrBody, String apiKey) {
        try {
            validateAccess(apiKey);
            log.info("Scanning from CodeReadrApp: {}", codeReadrBody);
            AssetImportDTO assetImportDTO = codeReadrImportMapper.mapFromCodeReadrRequest(codeReadrBody);
            AssetImport assetImport = assetImportService.importAssets(assetImportDTO);
            log.info("Scan ID: {}, imported successfully", assetImport.getScanId());
            return getCodeReadrResponse(assetImport);
        } catch (Exception e) {
            log.error("Error processing CodeReadr: {}", e.getMessage());
            return new CodeReadrResponse(new CodeReadrMessage(0, e.getMessage()));
        }
    }

    private CodeReadrResponse getCodeReadrResponse(AssetImport assetImport) {
        if (Objects.isNull(assetImport.getError())) {
            return new CodeReadrResponse(new CodeReadrMessage(1, "Thank you for scanning with codeREADr"));
        } else {
            return new CodeReadrResponse(new CodeReadrMessage(0, assetImport.getError()));
        }
    }

    private void validateAccess(String apiKey) {
        log.debug("Validating access");
        if (!Objects.equals(apiKey, this.apiKey)) {
            log.error("Access denied. ApiKey is missed or invalid.");
            throw new PermissionDeniedException("Access denied. ApiKey is missed or invalid.");
        }
        log.debug("Validating access success");
    }
}
