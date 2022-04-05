package com.nsoft.welcomebot.Utilities;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Credentials {

    @Value("${SLACK_BOT_TOKEN}")
    private String slackBotToken;

    @Value("${SLACK_SIGNING_SECRET}")
    private String slackSigningSecret;

    @Value("${OPEN_WEATHER_API_KEY}")
    private String openWeatherApiKey;

    @Value("${giphy.url}")
    private String giphyUrl;

}
