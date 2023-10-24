package com.bevis.asset.impl;

import com.bevis.asset.dto.FieldValueDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.bevis.common.util.CaseFormatUtil.toUnderscore;

@Component
class DynamicAssetMapper {

    public Map<String, FieldValueDTO>  toUnderscoreFields(Map<String, FieldValueDTO> fieldValues) {
        Map<String, FieldValueDTO> attrs = new HashMap<>();
        for (Map.Entry<String, FieldValueDTO> e : fieldValues.entrySet()) {
            attrs.put(toUnderscore(e.getKey()), e.getValue());
        }
        return attrs;
    }

}
