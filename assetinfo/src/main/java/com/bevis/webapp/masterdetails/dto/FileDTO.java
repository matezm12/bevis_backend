package com.bevis.webapp.masterdetails.dto;

import com.bevis.filecore.domain.enumeration.FileState;
import lombok.Data;

@Data
public class FileDTO {
    private Long id;
    private String fileName;
    private String ipfs;
    private String ipfsUrl;
    private String transactionId;
    private String transactionUrl;
    private Long block;
    private String sha256Hash;
    private String fileType;
    private Boolean encrypted;
    private FileState state;
    private Long fileSize;
}
