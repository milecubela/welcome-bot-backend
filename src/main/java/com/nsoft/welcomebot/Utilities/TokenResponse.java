package com.nsoft.welcomebot.Utilities;

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
