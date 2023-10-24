package com.bevis.certbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class FileItem {
    private final String ipfs;
    private final String ipfsUrl;
    private final String tx;
    private final String txUrl;
    private final String previewUrl;
    private final PreviewMode previewMode;
}
