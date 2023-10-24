package com.bevis.nftcore.repository;

import com.bevis.nftcore.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TokenRepository extends JpaRepository<Token, Long>, JpaSpecificationExecutor<Token> {
}
