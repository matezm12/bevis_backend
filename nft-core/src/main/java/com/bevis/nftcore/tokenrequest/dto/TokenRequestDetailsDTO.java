package com.bevis.nftcore.tokenrequest.dto;

import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.nftcore.domain.enumeration.TokenRequestStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class TokenRequestDetailsDTO {
    private Long id;
    private String code;
    private String name;
    private TokenRequestStatus status;
    private String tokenId;
    private Integer addressesProcessed = 0;
    private Integer addressesFailed = 0;
    private Integer addressesCount = 0;
    private Instant createdAt;
    private Instant updatedAt;
    private Blockchain blockchain;
}
