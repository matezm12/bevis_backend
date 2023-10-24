package com.bevis.filecode;

import com.bevis.filecode.domain.FileCode;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FileCodeService {

    String getCodeByFileType(String fileType);

    Optional<FileCode> getFileCodeByFileType(String fileType);

    Set<String> getSupportedExtensions();

    Map<String, Integer> getPriorityCodesMap();

    Map<String, FileCode> getFileCodesMap();
}
