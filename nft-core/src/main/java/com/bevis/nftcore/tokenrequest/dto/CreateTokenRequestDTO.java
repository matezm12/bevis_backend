package com.bevis.nftcore.tokenrequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateTokenRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private Long blockchainId;

    @NotNull
    private String tokenId;

    @NotNull
    private List<String> destinationAddresses;
}
