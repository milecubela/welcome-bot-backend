package com.nsoft.welcomebot.Security.AuthUtils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OauthProperties {

    @Value("${app.google.oauth.tokenvalidate}")
    private String validateGoogleTokenUrl;

    @Value("${app.google.oauth.tokenrevoke}")
    private String revokeGoogleTokenUrl;
}
