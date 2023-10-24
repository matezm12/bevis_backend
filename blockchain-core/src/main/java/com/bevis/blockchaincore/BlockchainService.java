package com.bevis.blockchaincore;

import com.bevis.blockchaincore.domain.Blockchain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BlockchainService {
    List<Blockchain> findAll();

    Page<Blockchain> findAll(Pageable pageable);

    Page<Blockchain> searchAll(String search, Pageable pageable);

    Optional<Blockchain> findById(Long id);

    Optional<Blockchain> findByName(String blockchainName);

    String fetchAssetIdFromPublicKey(String publicKey, Blockchain blockchain);

    Blockchain save(Blockchain blockchain);

    void delete(Long id);

    void updateSource(String blockchainName, String source);
}
