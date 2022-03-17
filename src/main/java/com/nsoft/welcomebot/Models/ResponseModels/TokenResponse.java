package com.nsoft.welcomebot.Models.ResponseModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponse {

    private String idToken;

    public TokenResponse(String idToken) {
        this.idToken = idToken;
    }
}
