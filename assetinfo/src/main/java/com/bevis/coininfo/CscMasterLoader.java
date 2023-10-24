package com.bevis.coininfo;

import com.bevis.master.domain.Master;

import java.util.List;
import java.util.Optional;

public interface CscMasterLoader {
    Master getCscMaster(String publicKey);
    Optional<Master> findCscMaster(String publicKey);
    List<Master> findAllCscMastersByPublicKeys(List<String> publicKeys);
}
