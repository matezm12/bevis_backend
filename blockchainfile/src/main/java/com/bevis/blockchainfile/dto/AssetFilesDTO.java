package com.bevis.blockchainfile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AssetFilesDTO {
    private FileDTO certificate;
    private List<FileDTO> files;
}
