package com.bevis.nftcore.tokenrequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SearchDTO {
    @NotNull
    private String search;
}
