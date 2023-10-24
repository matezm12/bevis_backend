package com.bevis.assetimport.codereadr;

import com.bevis.assetimport.AssetImportException;
import com.bevis.assetimport.AssetImportService;
import com.bevis.assetimport.dto.ArgumentsMapDTO;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.csv.CsvFileUploaderService;
import com.bevis.common.async.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CsvCodereadrImportServiceImpl implements CsvCodereadrImportService {
    private final AssetImportService assetImportService;
    private final CodeReadrImportMapper codeReadrImportMapper;
    private final CsvFileUploaderService csvFileUploaderService;
    private final AsyncService asyncService;

    @Override
    public void importFromCsv(MultipartFile file) {
        try {
            List<ArgumentsMapDTO> argumentsMap = csvFileUploaderService.loadObjectList(ArgumentsMapDTO.class, file);
            asyncService.run(() -> {
                try {
                    importCsvCodeReadrArgumentsMap(argumentsMap);
                } catch (AssetImportException e) {
                    log.error(e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void importCsvCodeReadrArgumentsMap(List<ArgumentsMapDTO> csvCodeReaderArgumentsMapList) throws AssetImportException {
        List<AssetImportDTO> assetImportDTOS = csvCodeReaderArgumentsMapList.stream()
                .map(codeReadrImportMapper::mapFromCodeReadrArguments)
                .collect(Collectors.toList());
        for (AssetImportDTO assetImportDTO : assetImportDTOS) {
            assetImportService.importAssets(assetImportDTO);
        }
    }

}
