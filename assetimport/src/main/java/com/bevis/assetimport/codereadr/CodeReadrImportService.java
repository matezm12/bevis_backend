package com.bevis.assetimport.codereadr;

import com.bevis.assetimport.dto.CodeReadrResponse;

public interface CodeReadrImportService {
    CodeReadrResponse importAssetsFromCodereadrBody(String codeReadrBody, String apiKey);
}
