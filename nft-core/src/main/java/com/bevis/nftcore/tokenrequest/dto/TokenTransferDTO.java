package com.bevis.nftcore.tokenrequest.dto;

import com.bevis.nftcore.domain.TokenRequest;
import lombok.Data;

import java.time.Instant;

@Data
public class TokenTransferDTO {
    private Long id;
    private String destinationAddress;
    private String transactionId;
    private String errorMessage;
    private Instant createdAt;
    private Instant updatedAt;
    private TokenRequest tokenRequest;
    private String transactionLink;
    private String addressLink;
}
