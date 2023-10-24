package com.bevis.assetimport;

import com.bevis.assetimport.dto.ImportWrappingDTO;

public interface CodeReadrImportEventHandler {
    void onAssetImportPrepared(ImportWrappingDTO importWrappingDTO);
}
