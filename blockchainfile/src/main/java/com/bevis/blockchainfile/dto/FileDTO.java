package com.bevis.blockchainfile.dto;

import com.bevis.filecode.domain.enumeration.FileCodeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private Long id;
    private String fileName;
    private String transactionId;
    private String ipfsHash;
    private String url;
    private String txUrl;
    private String fileType;
    private Boolean encrypted;
    private String placeholderUrl;
    private FileCodeGroup fileGroup;
}
