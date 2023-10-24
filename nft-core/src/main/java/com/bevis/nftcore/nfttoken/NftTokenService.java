package com.bevis.nftcore.nfttoken;

import com.bevis.nftcore.domain.NftToken;

import java.util.Optional;

public interface NftTokenService {
    Optional<NftToken> findById(String id);

    NftToken createOrUpdate(NftToken mapEntity);
}
