package com.nims.auth.controller;

import com.nims.auth.constants.ApiEndpoints;
import com.nims.auth.dto.LoginRequest;
import com.nims.auth.dto.TokenResponse;
import com.nims.auth.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.AUTH_BASE)
public class LoginController {


    private final LoginService authService;

    @PostMapping(ApiEndpoints.LOGIN)
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return authService.login(request, response);
    }

    @PostMapping(ApiEndpoints.REFRESH)
    public TokenResponse refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        return authService.refreshToken(refreshToken, response);
    }

    @PostMapping(ApiEndpoints.LOGOUT)
    public void logout(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        authService.logout(refreshToken, response);
    }

    @GetMapping(ApiEndpoints.WELL_KNOWN_JWKS_JSON)
    public Map<String, Object> getPublicKeys() {
        return authService.getJwkSet();
    }

}
