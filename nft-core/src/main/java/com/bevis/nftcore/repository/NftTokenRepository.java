package com.bevis.nftcore.repository;

import com.bevis.nftcore.domain.NftToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NftTokenRepository extends JpaRepository<NftToken, String> {
}
