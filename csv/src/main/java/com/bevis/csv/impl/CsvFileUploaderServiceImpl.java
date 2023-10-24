package com.bevis.csv.impl;

import com.bevis.csv.CsvException;
import com.bevis.csv.CsvFileParserMapper;
import com.bevis.csv.CsvFileUploaderService;
import com.bevis.files.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
final class CsvFileUploaderServiceImpl implements CsvFileUploaderService {
    private final CsvFileParserMapper csvFileParserMapper;

    @Override
    public <T> List<T> loadObjectList(Class<T> type, MultipartFile multipartFile) {
        log.debug("Start CSV file processing...");
        java.io.File uploadedFile = null;
        try {
            uploadedFile = java.io.File.createTempFile("file_", ".csv");
            log.debug("Created temp file for csv uploading");
            FileUtil.upload(multipartFile, uploadedFile);
            log.debug("CSV file uploaded");
            List<T> records = csvFileParserMapper.loadObjectList(type, uploadedFile);
            log.debug("Loaded records from csv {}", records);
            log.info("CSV file processed successfully.");
            return records;
        } catch (IOException e) {
            log.error("Error file processing");
            throw new CsvException("Error file processing", e);
        } finally {
            if (Objects.nonNull(uploadedFile)) {
                boolean delete = uploadedFile.delete();
                log.debug("Temp csv file delete status : {}", delete);
            }
        }
    }
}
