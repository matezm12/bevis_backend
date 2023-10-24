package com.bevis.bevisassetpush.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDataDTO {
    private String ipfs;
    private String fileType;
}
