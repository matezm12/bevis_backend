package com.bevis.nftcore.tokenrequest;

import com.bevis.nftcore.domain.Token;
import com.bevis.nftcore.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bevis.nftcore.tokenrequest.specification.TokenSpecification.bySearchRequest;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Token> findAll(String search, Pageable pageable) {
        return tokenRepository.findAll(bySearchRequest(search), pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Token> findById(Long id) {
        return tokenRepository.findById(id);
    }

    @Override
    public Token create(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Token update(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public void deleteById(Long id) {
        tokenRepository.deleteById(id);
    }
}
