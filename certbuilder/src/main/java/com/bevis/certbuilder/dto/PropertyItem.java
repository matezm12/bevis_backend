package com.bevis.certbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class PropertyItem {
    private final String title;
    private final String value;
    private final String url;
}
