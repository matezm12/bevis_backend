package com.bevis.filecore.dto;

import com.bevis.filecore.domain.enumeration.FileState;
import lombok.Data;

@Data
public class FileFilter {
    private String search;
    private String fileType;
    private Boolean onlyEncrypted;
    private FileState state;
    private String blockchain;
}
