package com.nims.auth.service;

import com.nims.auth.dto.LoginRequest;
import com.nims.auth.dto.TokenResponse;
import com.nims.auth.security.jwt.JwtProperties;
import com.nims.auth.security.token.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final JwtProperties props;

    public TokenResponse login(LoginRequest req, HttpServletResponse response) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        String username = auth.getName();
        var tokens = tokenService.issueTokens(username);

        // Set refresh token cookie
        setRefreshCookie(response, tokens.refreshToken());

        // Parse TTL from ISO-8601 duration (e.g., PT5M → 300 seconds)
        long expiresIn = Duration.parse(props.getAccessTokenTtl()).toSeconds();

        return TokenResponse.builder()
                .accessToken(tokens.accessToken())
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .refreshToken(tokens.refreshToken())
                .scope("openid profile email") // can be dynamic based on roles
                .idToken(tokens.idToken())     // extend tokenService to issue ID token
                .build();
    }


    public TokenResponse refreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Missing refresh token");
        }
        var tokens = tokenService.rotateRefreshToken(refreshToken);
        setRefreshCookie(response, tokens.refreshToken());
        return TokenResponse.builder()
                .accessToken(tokens.accessToken())
                .tokenType("Bearer")
                .expiresIn(Duration.parse(props.getAccessTokenTtl()).toSeconds())
                .refreshToken(tokens.refreshToken())
                .scope("openid profile email") // can be dynamic based on roles
                .idToken(tokens.idToken())     // extend tokenService to issue ID token
                .build();
    }

    public void logout(String refreshToken, HttpServletResponse response) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            // Best practice: revoke all user tokens on explicit logout
            // (You’d look up the token, get userId, and delete their tokens) //
            // tokenService.revokeUserTokens(userIdFromToken);
        }
        clearRefreshCookie(response);
    }

    public Map<String, Object> getJwkSet() {
        return null;
    }

    private void setRefreshCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(props.isCookieSecure());
        cookie.setPath("/");
        cookie.setDomain(props.getCookieDomain());
        cookie.setMaxAge((int) java.time.Duration.parse(props.getRefreshTokenTtl()).toSeconds());
        response.addCookie(cookie);
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(props.isCookieSecure());
        cookie.setPath("/");
        cookie.setDomain(props.getCookieDomain());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
