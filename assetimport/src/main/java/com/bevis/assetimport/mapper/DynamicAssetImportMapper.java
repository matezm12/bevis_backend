package com.bevis.assetimport.mapper;

import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.domain.AssetImport;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DynamicAssetImportMapper {

    public AssetImport mapAssetImport(AssetImport assetImport, AssetImportDTO assetImportDTO) {
        assetImport.setServiceId(assetImportDTO.getServiceId());
        assetImport.setUsername(assetImportDTO.getUsername());
        assetImport.setScanId(assetImportDTO.getScanId());
        assetImport.setDeviceId(assetImportDTO.getDeviceId());
        assetImport.setBarcode(assetImportDTO.getBarcode());
        assetImport.setScanDate(assetImportDTO.getScanTime());
        assetImport.setUploadDate(assetImportDTO.getUploadTime());
        assetImport.setCodereadrBody(assetImportDTO.getCodereadrBody());

        //questions
        Map<String, String> questionArgs = assetImportDTO.getArguments();
        assetImport.setAttributes(new HashMap<>(questionArgs));

        assetImport.setUpc(assetImportDTO.getQuestion("productUpc"));

        return assetImport;
    }

    public AssetImportDTO mapFromEntity(AssetImport assetImport) {
        AssetImportDTO assetImportDTO = new AssetImportDTO();
        assetImportDTO.setServiceId(assetImport.getServiceId());
        assetImportDTO.setScanId(assetImport.getScanId());
        assetImportDTO.setDeviceId(assetImport.getDeviceId());
        assetImportDTO.setScanTime(assetImport.getScanDate());
        assetImportDTO.setUploadTime(assetImport.getUploadDate());
        assetImportDTO.setUsername(assetImport.getUsername());
        String barcode = assetImport.getBarcode();
        assetImportDTO.setBarcode(barcode);
        assetImportDTO.setCodereadrBody(assetImport.getCodereadrBody());

        String[] barcodeData = barcode.split("\n");
        assetImportDTO.setBarcodeItems(Arrays.stream(barcodeData).map(x -> x.replace("\r", "")).collect(Collectors.toList()));

        assetImportDTO.setArguments(mapConvert(assetImport.getAttributes()));

        return assetImportDTO;
    }

    private Map<String, String> mapConvert(Map<String, Object> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue()));
    }
}
