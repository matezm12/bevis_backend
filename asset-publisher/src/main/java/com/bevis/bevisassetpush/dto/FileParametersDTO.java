package com.bevis.bevisassetpush.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileParametersDTO {
    private long fileSizeInBytes;
    private String extension;
    private String fileName;
}
