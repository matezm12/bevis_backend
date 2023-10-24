package com.bevis.nftcore.nfttoken;

import com.bevis.nftcore.domain.NftToken;
import com.bevis.nftcore.repository.NftTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NftTokenServiceImpl implements NftTokenService {

    private final NftTokenRepository nftTokenRepository;

    @Override
    public Optional<NftToken> findById(String id) {
        return nftTokenRepository.findById(id);
    }

    @Override
    public NftToken createOrUpdate(NftToken nftToken) {
        return nftTokenRepository.saveAndFlush(nftToken);
    }

}
