package com.bevis.balancecore.impl;

import com.bevis.balancecore.CoinBalanceSourceService;
import com.bevis.balancecore.dto.CoinBalanceSourceUpdateEvent;
import com.bevis.balancecore.domain.CoinBalanceSource;
import com.bevis.balancecore.repository.CoinBalanceSourceRepository;
import com.bevis.events.EventPublishingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.bevis.balancecore.specification.CoinBalanceSourceSpecification.bySearchQuery;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
class CoinBalanceSourceServiceImpl implements CoinBalanceSourceService {

    private final CoinBalanceSourceRepository coinBalanceSourceRepository;
    private final EventPublishingService eventPublishingService;

    @Override
    public List<CoinBalanceSource> findAll() {
        log.debug("findAll");
        return coinBalanceSourceRepository.findAll();
    }

    @Override
    public List<CoinBalanceSource> findAll(Boolean isLive) {
        return coinBalanceSourceRepository.findAllByLive(isLive);
    }

    @Override
    public Page<CoinBalanceSource> findAll(Pageable pageable) {
        return coinBalanceSourceRepository.findAll(pageable);
    }

    @Override
    public Page<CoinBalanceSource> searchAll(String search, Pageable pageable) {
        return coinBalanceSourceRepository.findAll(bySearchQuery(search), pageable);
    }

    @Override
    public Optional<CoinBalanceSource> findById(Long id) {
        return coinBalanceSourceRepository.findById(id);
    }

    @Override
    public CoinBalanceSource save(CoinBalanceSource coinBalanceSource) {
        CoinBalanceSource updatedSource = coinBalanceSourceRepository.saveAndFlush(coinBalanceSource);
        eventPublishingService.publishEvent(CoinBalanceSourceUpdateEvent.builder()
                .coinBalanceSource(updatedSource)
                .build());
        return updatedSource;
    }

    @Override
    public void deleteById(Long id) {
        coinBalanceSourceRepository.deleteById(id);
    }


}
