package com.bevis.certbuilder.dto;

import lombok.Data;

@Data(staticConstructor = "of")
public class SectionItem {
    private final String id;
    private final String name;
}
