package com.bevis.blockchainfile;

import com.bevis.blockchaincore.domain.Blockchain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlockchainManagementService {
    Page<Blockchain> findAll(Pageable pageable);
}
