package com.bevis.assetimport.codereadr;

import org.springframework.web.multipart.MultipartFile;

public interface CsvCodereadrImportService {
    void importFromCsv(MultipartFile file);
}
