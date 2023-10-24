package com.bevis.bevisassetpush.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IpfsDTO {
    @NotNull
    private String ipfs;
    @NotNull
    private String fileExtension;
}
