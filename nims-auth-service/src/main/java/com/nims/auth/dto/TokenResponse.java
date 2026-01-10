package com.nims.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({
        "access_token",
        "token_type",
        "expires_in",
        "refresh_token",
        "scope",
        "id_token"
})
public class TokenResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("expires_in")
    long expiresIn;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("scope")
    String scope;

    @JsonProperty("id_token")
    String idToken;
}
