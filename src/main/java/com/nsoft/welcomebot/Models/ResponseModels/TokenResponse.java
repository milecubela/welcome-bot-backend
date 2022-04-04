package com.nsoft.welcomebot.Models.ResponseModels;

import com.nsoft.welcomebot.Security.AuthUtils.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponse {

    private String idToken;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
