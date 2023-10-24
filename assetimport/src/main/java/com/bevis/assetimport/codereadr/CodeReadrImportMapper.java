package com.bevis.assetimport.codereadr;

import com.bevis.assetimport.dto.CsvCodeReaderRecord;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.util.CodeReadrBodyConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bevis.assetimport.util.DateConverter.convertReaderDateToInstant;

@Component
@Slf4j
class CodeReadrImportMapper {

    private static final String QUESTION_PREFIX = "Question: ";

    public AssetImportDTO mapFromCodeReadrRequest(String data) {
        Map<String, String> arguments = CodeReadrBodyConverterUtil.getCodeReadrArgumentsMap(data);
        if (arguments == null) return null;
        AssetImportDTO assetImportDTO = mapFromCodeReadrArguments(arguments);
        assetImportDTO.setCodereadrBody(data);
        return assetImportDTO;
    }

    public AssetImportDTO mapFromCodeReadrArguments(Map<String, String> arguments) {
        CsvCodeReaderRecord csvCodeReaderRecord = DeserializerUtil.deserialize(arguments, CsvCodeReaderRecord.class);
        AssetImportDTO assetImportDTO = toDto(csvCodeReaderRecord);
        Map<String, String> questionArguments = arguments
                .entrySet().stream()
                .filter(x -> x.getKey().startsWith(QUESTION_PREFIX))
                .collect(Collectors.toMap(e1 -> e1.getKey().substring(QUESTION_PREFIX.length()), Map.Entry::getValue));
        assetImportDTO.setArguments(questionArguments);
        return assetImportDTO;
    }

    public List<AssetImportDTO> toDto(List<CsvCodeReaderRecord> records){
        if (records == null){
            return null;
        }
        return records.stream().map(this::toDto).collect(Collectors.toList());
    }

    public AssetImportDTO toDto(CsvCodeReaderRecord record){
        if (record == null){
            return null;
        }
        final AssetImportDTO dto = new AssetImportDTO();
        dto.setServiceId(record.getServiceId());
        dto.setScanId(record.getScanId());
        try {
            dto.setDeviceId(Long.valueOf(record.getDeviceId()));
        } catch (Exception e){
            log.error("DeviceId is not Long format");
            e.printStackTrace();
        }

        final String barcode = record.getBarcode();
        String[] barcodeData = barcode.split("\n");
        dto.setBarcodeItems(Arrays.stream(barcodeData).map(x -> x.replace("\r", "")).collect(Collectors.toList()));
        dto.setBarcode(record.getBarcode());
        dto.setScanTime(convertReaderDateToInstant(record.getTimestampScanned()));
        dto.setUploadTime(convertReaderDateToInstant(record.getTimestampReceived()));
        dto.setUsername(record.getUserName());

        return dto;
    }
}
