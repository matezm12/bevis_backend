package com.bevis.master.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class MasterImportDTO {
    private Long id;
    private String name;
    private String keyType;
    private Long qty;
    private Instant date;
}
