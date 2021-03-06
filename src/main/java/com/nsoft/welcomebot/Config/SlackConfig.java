package com.nsoft.welcomebot.Config;

import com.nsoft.welcomebot.Services.SlackService;
import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackCommandsCache;
import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackEventsCache;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class SlackConfig {

    private final Credentials crd;
    private final SlackEventsCache slackEventsFactory;
    private final SlackCommandsCache slackCommandsCache;

    @Bean
    public SlackService slackService(App app) {
        SlackService slackService = new SlackService(crd, app, slackEventsFactory, slackCommandsCache);
        slackService.subscribeAll();
        return slackService;
    }

    @Bean
    public App app() {
        AppConfig appConfig = new AppConfig();
        appConfig.setSigningSecret(crd.getSlackSigningSecret());
        appConfig.setSingleTeamBotToken(crd.getSlackBotToken());
        return new App(appConfig);
    }
}
