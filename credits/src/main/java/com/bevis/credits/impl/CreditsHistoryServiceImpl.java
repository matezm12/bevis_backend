package com.bevis.credits.impl;

import com.bevis.user.domain.User;
import com.bevis.credits.repository.CreditsBalanceRepository;
import com.bevis.credits.CreditsHistoryService;
import com.bevis.credits.dto.CreditsHistoryItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class CreditsHistoryServiceImpl implements CreditsHistoryService {

    private final CreditsBalanceRepository creditsBalanceRepository;
    private final CreditsHistoryMapper creditsHistoryMapper;

    @Override
    public Page<CreditsHistoryItem> getUserBalanceHistory(User user, Pageable pageable) {
        return creditsBalanceRepository.findAllByUserOrderByDateTimeDesc(user, pageable)
                .map(creditsHistoryMapper::map);
    }
}
