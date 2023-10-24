package com.bevis.blockchainfile;

import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.blockchaincore.BlockchainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BlockchainManagementServiceImpl implements BlockchainManagementService {

    private final BlockchainRepository blockchainRepository;

    @Override
    public Page<Blockchain> findAll(Pageable pageable) {
        return blockchainRepository.findAll(pageable);
    }
}
