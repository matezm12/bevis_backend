package com.bevis.csv;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvFileUploaderService {

    <T> List<T> loadObjectList(Class<T> type, MultipartFile multipartFile);
}
