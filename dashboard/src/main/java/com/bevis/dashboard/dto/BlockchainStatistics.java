package com.bevis.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class BlockchainStatistics {
    private List<BlockchainItemStatistic> blockchainStatistics;
}
