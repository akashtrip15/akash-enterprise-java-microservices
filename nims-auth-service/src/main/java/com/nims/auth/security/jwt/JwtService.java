package com.nims.auth.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties props;
    private final ResourceLoader resourceLoader;

    private volatile PrivateKey privateKey;
    private volatile PublicKey publicKey;

    private PrivateKey getPrivateKey() {
        if (privateKey == null) {
            synchronized (this) {
                if (privateKey == null) {
                    try {
                        KeyStore ks = KeyStore.getInstance("PKCS12");
                        InputStream is = resourceLoader.getResource(props.getKeystore().getPath()).getInputStream();
                        ks.load(is, props.getKeystore().getPassword().toCharArray());
                        Key key = ks.getKey(props.getKeystore().getAlias(), props.getKeystore().getPassword().toCharArray());
                        privateKey = (PrivateKey) key;
                        publicKey = ks.getCertificate(props.getKeystore().getAlias()).getPublicKey();
                    } catch (Exception e) {
                        throw new IllegalStateException("Failed to load keystore", e);
                    }
                }
            }
        }
        return privateKey;
    }

    private PublicKey getPublicKey() {
        if (publicKey == null) getPrivateKey();
        return publicKey;
    }

    public String generateAccessToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.parse(props.getAccessTokenTtl()));
        return Jwts.builder()
                .header().type(Header.JWT_TYPE).and()
                .issuer(props.getIssuer())
                .audience().add(props.getAudience()).and()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    public Jws<Claims> parseAndValidate(String jwt) {

        return Jwts.parser()
                .verifyWith(getPublicKey())
                .requireIssuer(props.getIssuer())
                .build()
                .parseSignedClaims(jwt);
    }

    public boolean isExpired(Claims claims) {
        Date exp = claims.getExpiration();
        return exp == null || exp.before(new Date());
    }
}
