package com.bevis.dashboard;

import com.bevis.dashboard.dto.BlockchainStatistics;
import com.bevis.dashboard.dto.UsersStatistic;

public interface DashboardManagementService {
    UsersStatistic loadDashboardUsersStatistic();

    BlockchainStatistics loadDashboardBlockchainStatisticsInfo();
}
