package com.nsoft.welcomebot.Config;

import com.slack.api.bolt.App;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    @Bean
    public App initApp() {
        App app = new App();
        return app;
    }
}
