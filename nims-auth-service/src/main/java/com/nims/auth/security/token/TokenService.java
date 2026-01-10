package com.nims.auth.security.token;

import com.nims.auth.entity.RefreshToken;
import com.nims.auth.repository.RefreshTokenRepository;
import com.nims.auth.repository.UserRepository;
import com.nims.auth.security.jwt.JwtProperties;
import com.nims.auth.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public record Tokens(String accessToken, String refreshToken, String idToken) {
    }

    public Tokens issueTokens(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        String accessToken = generateJwt(user.getEmail(), baseClaims(user, false));
        String refreshToken = createAndPersistRefreshToken(user.getId());
        String idToken = generateJwt(user.getEmail(), baseClaims(user, true));

        return new Tokens(accessToken, refreshToken, idToken);
    }

    /**
     * Rotate an existing refresh token: invalidate old one and issue new tokens.
     */
    public Tokens rotateRefreshToken(String oldRefreshToken) {
        var existing = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (isInvalid(existing)) {
            throw new IllegalStateException("Invalid or expired refresh token");
        }

        existing.setRotated(true);
        refreshTokenRepository.save(existing);

        var user = userRepository.findById(existing.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for refresh token"));

        String newAccessToken = generateJwt(user.getEmail(), baseClaims(user, false));
        String newRefreshToken = createAndPersistRefreshToken(user.getId());
        String newIdToken = generateJwt(user.getEmail(), baseClaims(user, true));

        return new Tokens(newAccessToken, newRefreshToken, newIdToken);
    }

    /**
     * Revoke all refresh tokens for a given user.
     */
    public void revokeUserTokens(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    /**
     * Common claims for access/ID tokens.
     *
     * @param includeEmail whether to include email claim (ID token style).
     */
    private Map<String, Object> baseClaims(com.nims.auth.entity.User user, boolean includeEmail) {
        var claims = Map.of(
                "uid", user.getId(),
                "roles", user.getRoles(),
                "provider", user.getProvider().name()
        );
        if (includeEmail) {
            // add email claim for ID token
            return new java.util.HashMap<>(claims) {{
                put("email", user.getEmail());
            }};
        }
        return claims;
    }

    private String generateJwt(String subject, Map<String, Object> claims) {
        return jwtService.generateAccessToken(subject, claims);
    }

    private String createAndPersistRefreshToken(Long userId) {
        String rawToken = UUID.randomUUID() + "." + UUID.randomUUID();
        Instant now = Instant.now();
        Instant expiry = now.plus(Duration.parse(jwtProperties.getRefreshTokenTtl()));

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(rawToken)
                .issuedAt(now)
                .expiresAt(expiry)
                .revoked(false)
                .rotated(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private boolean isInvalid(RefreshToken token) {
        return token.isRevoked()
                || token.isRotated()
                || token.getExpiresAt().isBefore(Instant.now());
    }
    
}
