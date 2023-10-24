package com.bevis.master.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterImportRequest {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String keyType;

    @NonNull
    private Integer qty;

}
