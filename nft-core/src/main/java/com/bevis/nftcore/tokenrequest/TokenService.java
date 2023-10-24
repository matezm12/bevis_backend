package com.bevis.nftcore.tokenrequest;

import com.bevis.nftcore.domain.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TokenService {
    Page<Token> findAll(String search, Pageable pageable);

    Optional<Token> findById(Long id);

    Token create(Token token);

    Token update(Token token);

    void deleteById(Long id);
}
