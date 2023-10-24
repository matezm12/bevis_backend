package com.bevis.files.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {
    private String fileName;
    private java.io.File file;
}
