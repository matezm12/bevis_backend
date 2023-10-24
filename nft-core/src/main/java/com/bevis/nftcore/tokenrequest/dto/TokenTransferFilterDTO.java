package com.bevis.nftcore.tokenrequest.dto;

import com.bevis.nftcore.domain.enumeration.TokenRequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TokenTransferFilterDTO {

    @JsonIgnore
    private Long requestId;

    private TokenRequestStatus filterState;
}
