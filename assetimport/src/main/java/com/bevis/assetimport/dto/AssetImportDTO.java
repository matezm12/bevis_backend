package com.bevis.assetimport.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.bevis.common.util.CaseFormatUtil.makeUnderscoreFields;

@Data
public class AssetImportDTO {

    private String serviceId;
    private String scanId;
    private Long deviceId;
    private Instant scanTime;
    private Instant uploadTime;
    private String username;

    private String barcode;
    private String codereadrBody;
    private List<String> barcodeItems;
    private Map<String, String> arguments;

    public String getQuestion(String key) {
        return arguments != null ? arguments.get(key) : null;
    }

    public Map<String, String> getFormatterQuestionArgs() {
        return makeUnderscoreFields(arguments);
    }

}
