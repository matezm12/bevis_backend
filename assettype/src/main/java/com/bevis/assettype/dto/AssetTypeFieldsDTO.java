package com.bevis.assettype.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class AssetTypeFieldsDTO {
    private final String fieldKey;
    private final String fieldName;
    private final String title;
    private final String type;
    private final List<String> select;
    private final String hint;
    private final String validation;
}
