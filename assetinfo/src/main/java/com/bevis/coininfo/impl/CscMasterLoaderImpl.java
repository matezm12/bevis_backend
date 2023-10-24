package com.bevis.coininfo.impl;

import com.bevis.coininfo.CscMasterLoader;
import com.bevis.coininfo.NonCscTypeException;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.master.domain.Master;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class CscMasterLoaderImpl implements CscMasterLoader {

    private final MasterService masterService;

    @Transactional(readOnly = true)
    @Override
    public Master getCscMaster(String publicKey) {
        Master master = getMasterOpt(publicKey)
                .orElseThrow(() -> new ObjectNotFoundException("Public key \"" + publicKey + "\" not found."));
        if (Objects.isNull(master.getIsCsc()) || !master.getIsCsc()) {
            throw new NonCscTypeException("Asset \"" + publicKey + "\" is not a Cold Storage Coin!");
        }
        return master;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Master> findCscMaster(String publicKey) {
        return getMasterOpt(publicKey)
                .filter(Master::getIsCsc);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Master> findAllCscMastersByPublicKeys(List<String> publicKeys) {
        List<Master> masters = masterService.findAllByPublicKeys(publicKeys);
        return masters.stream()
                .filter(Master::getIsCsc)
                .collect(Collectors.toList());
    }

    private Optional<Master> getMasterOpt(String publicKey) {
        return masterService.findByIdOrPublicKey(publicKey);
    }
}
