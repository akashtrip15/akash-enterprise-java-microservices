package com.nims.auth.security.jwt;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String issuer;
    private String audience;
    private String accessTokenTtl;   // ISO-8601 duration, e.g., PT5M
    private String refreshTokenTtl;  // ISO-8601 duration, e.g., P14D
    private Keystore keystore = new Keystore();
    private String cookieDomain;
    private boolean cookieSecure;

    @Data
    public static class Keystore {
        private String path;
        private String alias;
        private String password;
    }
}

