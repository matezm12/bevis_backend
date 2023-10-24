package com.bevis.coininfo.impl;

import com.bevis.coininfo.CoinActivationService;
import com.bevis.coininfo.CscMasterLoader;
import com.bevis.coininfo.dto.CoinActivationRequest;
import com.bevis.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
class CoinActivationServiceImpl implements CoinActivationService {

    private final CscMasterLoader cscMasterLoader;

    @Transactional
    @Override
    public void activateCoin(CoinActivationRequest coinActivationRequest) {
        cscMasterLoader
                .findCscMaster(coinActivationRequest.getPublicKey())
                .filter(x -> !Objects.equals(true, x.getActivationStatus()))
                .map(x -> {
                    x.setActivationStatus(true);
                    return x;
                }).orElseThrow(() -> {
            throw new BaseException("Can not activate the coin");
        });
    }

    @Transactional
    @Override
    public void deactivateCoin(CoinActivationRequest coinActivationRequest) {
        cscMasterLoader
                .findCscMaster(coinActivationRequest.getPublicKey())
                .map(x -> {
                    x.setActivationStatus(false);
                    return x;
                }).orElseThrow(() -> {
            throw new BaseException("Can not deactivate the coin");
        });
    }
}
