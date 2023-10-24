package com.bevis.certbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class GroupItem {
    private final String key;
    private final String name;
    private final String section;
    private final List<PropertyItem> properties;
    private final List<FileItem> files;
}
