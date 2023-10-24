package com.bevis.security;

import com.bevis.security.dto.JwtToken;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
class TokenProviderImpl implements TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_TYPE = "token_type";
    private static final String USER_ASSET_ID = "USER_ASSET_ID";
    private static final String GROUP_ASSET_ID = "GROUP_ASSET_ID";

    private enum TokenType{
        TOKEN_TYPE_ACCESS,
        TOKEN_TYPE_REFRESH
    }

    @Value("${security.authentication.jwt.secret}")
    private String secretKey;

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    @Value("${security.authentication.jwt.token-validity-in-seconds-for-remember-me}")
    private long tokenValidityInSecondsForRememberMe;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    @PostConstruct
    public void init() {
        this.tokenValidityInMilliseconds = 1000 * tokenValidityInSeconds;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * tokenValidityInSecondsForRememberMe;
    }

    @Override
    public JwtToken createJwtToken(Authentication authentication, boolean rememberMe) {
        return JwtToken.builder()
                .accessToken(createToken(authentication, rememberMe))
                .refreshToken(createRefreshToken(authentication, rememberMe))
                .build();
    }

    private String createRefreshToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        log.trace("Token authorities : {}", authorities);
        Date validity = getRefreshValidityDate(getNowTime(), rememberMe);
        log.trace("Token validity: {}", validity.toString());
        return buildToken(authentication, validity, authorities, TokenType.TOKEN_TYPE_REFRESH);
    }

    @Override
    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        log.trace("Token authorities : {}", authorities);
        Date validity = getValidityDate(getNowTime(), rememberMe);
        log.trace("Token validity: {}", validity.toString());
        return buildToken(authentication, validity, authorities, TokenType.TOKEN_TYPE_ACCESS);
    }

    @Override
    public Authentication getAuthentication(String token) {
        return getAuthentication(token, TokenType.TOKEN_TYPE_ACCESS);
    }

    @Override
    public Authentication getAuthenticationFromRefreshToken(String refreshToken) {
        return getAuthentication(refreshToken, TokenType.TOKEN_TYPE_REFRESH);
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.debug("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.debug("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e.getMessage());
        }
        return false;
    }

    private Authentication getAuthentication(String token, TokenType tokenTypeAccess) {
        Claims claims = getTokenClaims(token);
        String tokenType = claims.get(TOKEN_TYPE).toString();
        if (!Objects.equals(tokenType, tokenTypeAccess.name())) {
            throw new AccessDeniedException("Token type not valid");
        }
        Collection<? extends GrantedAuthority> authorities = getAuthoritiesFromClaims(claims);
        String userAssetId = Optional.ofNullable(claims.get(USER_ASSET_ID))
                .map(Object::toString).orElse(null);
        String groupAssetId = Optional.ofNullable(claims.get(GROUP_ASSET_ID))
                .map(Object::toString).orElse(null);
        User principal = new BevisUser(claims.getSubject(), "", authorities)
                .userAssetId(userAssetId).groupAssetId(groupAssetId);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private String buildToken(Authentication authentication, Date validity, String authorities, TokenType tokenType) {
        String tokenSubject = authentication.getName();
        log.trace("Token subject: {}", tokenSubject);
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities);
        claims.put(TOKEN_TYPE, tokenType.name());
        if (authentication.getPrincipal() instanceof BevisUser) {
            BevisUser bevisUser = (BevisUser) authentication.getPrincipal();
            claims.put(USER_ASSET_ID, bevisUser.getUserAssetId());
            claims.put(GROUP_ASSET_ID, bevisUser.getGroupAssetId());
        }
        return Jwts.builder()
                .setSubject(tokenSubject)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(validity)
                .compact();
    }

    private List<SimpleGrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    private Claims getTokenClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getValidityDate(long now, boolean rememberMe) {
        if (rememberMe) {
            return new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            return new Date(now + this.tokenValidityInMilliseconds);
        }
    }

    private Date getRefreshValidityDate(long now, boolean rememberMe) {
        return new Date(now + this.tokenValidityInMillisecondsForRememberMe);
    }

    private long getNowTime() {
        return (new Date()).getTime();
    }
}
