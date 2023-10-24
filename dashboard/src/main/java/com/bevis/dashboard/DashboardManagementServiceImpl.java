package com.bevis.dashboard;

import com.bevis.blockchain.blockchain.BlockchainGatewayService;
import com.bevis.blockchaincore.BlockchainRepository;
import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.dashboard.dto.BlockchainItemStatistic;
import com.bevis.dashboard.dto.BlockchainStatistics;
import com.bevis.dashboard.dto.UsersStatistic;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class DashboardManagementServiceImpl implements DashboardManagementService {

    private final UserRepository userRepository;

    private final BlockchainRepository blockchainRepository;

    private final BlockchainGatewayService blockchainGatewayService;

    private final BlockchainUrlGateway urlGatewayService;

    @Override
    public UsersStatistic loadDashboardUsersStatistic() {
        UsersStatistic usersStatistic = new UsersStatistic();
        usersStatistic.setUsersCount(userRepository.count());
        return usersStatistic;
    }

    @Override
    public BlockchainStatistics loadDashboardBlockchainStatisticsInfo() {
        List<Blockchain> blockchains = blockchainRepository.findAll();
        return BlockchainStatistics.of(
                blockchains.stream()
                        .filter(x-> Strings.isNotBlank(x.getNodeAddress()))
                        .map(this::getBlockchainItemStatistic).collect(Collectors.toList())
        );
    }

    private BlockchainItemStatistic getBlockchainItemStatistic(Blockchain blockchain) {
        BlockchainItemStatistic blockchainItemStatistic = new BlockchainItemStatistic();
        String blockchainName = blockchain.getName();
        blockchainItemStatistic.setName(blockchainName);
        blockchainItemStatistic.setAddress(blockchain.getNodeAddress());
        blockchainItemStatistic.setAddressUrl(urlGatewayService.getBlockchainAddressLink(blockchain.getNodeAddress(), blockchain));

        String balance = "N/A";
        try {
            if (Objects.nonNull(blockchain.getNodeAddress())) {
                balance = blockchainGatewayService.getBalance(blockchainName);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        blockchainItemStatistic.setBalance(balance);
        return blockchainItemStatistic;
    }
}
