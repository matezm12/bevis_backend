package com.bevis.appbe.web.rest.admin;

import com.bevis.dashboard.DashboardManagementService;
import com.bevis.dashboard.dto.BlockchainStatistics;
import com.bevis.dashboard.dto.UsersStatistic;
import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.balancecore.CoinBalanceStatisticsService;
import com.bevis.balancecore.dto.CoinBalanceItem;
import com.bevis.balancecore.dto.CoinStatisticItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminDashboardController {

    private final DashboardManagementService dashboardManagementService;
    private final CoinBalanceStatisticsService coinBalanceStatisticsService;

    @Secured(ADMIN)
    @GetMapping("admin/dashboard/users-statistic")
    UsersStatistic loadDashboardUsersStatisticInfo() {
        log.debug("REST to load dashboard users-statistic...");
        return dashboardManagementService.loadDashboardUsersStatistic();
    }

    @Secured(ADMIN)
    @GetMapping("admin/dashboard/blockchains")
    BlockchainStatistics loadDashboardBlockchainStatisticsInfo() {
        log.debug("REST to load dashboard blockchains...");
        return dashboardManagementService.loadDashboardBlockchainStatisticsInfo();
    }

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/dashboard/coin-balances-stats")
    List<CoinStatisticItem> loadAllStatistic() {
        log.debug("REST to load coin-balances-stats...");
        return coinBalanceStatisticsService.loadAllStatistic();
    }

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/dashboard/real-crypto-balance")
    List<CoinBalanceItem> loadBalances(@RequestParam String currency) {
        log.debug("REST to load coin-balances-stats...");
        return coinBalanceStatisticsService.loadBalances(currency);
    }

}
