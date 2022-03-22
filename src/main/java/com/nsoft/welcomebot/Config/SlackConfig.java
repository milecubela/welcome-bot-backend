package com.nsoft.welcomebot.Config;

import com.nsoft.welcomebot.Utilities.Credentials;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SlackConfig {

    private final Credentials crd;

    @Bean
    public App initApp() {
        AppConfig appConfig = new AppConfig();
        appConfig.setSigningSecret(crd.getSlackSigningSecret());
        appConfig.setSingleTeamBotToken(crd.getSlackBotToken());
        App app = new App(appConfig);
        return app;
    }
}
