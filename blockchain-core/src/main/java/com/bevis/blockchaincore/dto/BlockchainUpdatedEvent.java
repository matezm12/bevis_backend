package com.bevis.blockchaincore.dto;

import com.bevis.blockchaincore.domain.Blockchain;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockchainUpdatedEvent {
    private Blockchain blockchain;
}
