package com.bevis.assetimport.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class BarcodeUtil {

    public static Optional<String> getUpcFromBarcode(List<String> barcodeItems) {
        return barcodeItems.stream()
                .filter(ImportMatcherUtil::matchUpc)
                .findFirst();
    }

    public static List<String> getAssetsFromBarcode(List<String> barcodeItems) {
        return barcodeItems.stream()
                .filter(ImportMatcherUtil::matchPublicKey)
                .collect(Collectors.toList());
    }
}
