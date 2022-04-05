package com.nsoft.welcomebot.SlackModule.Logger;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

@Slf4j
public class SlackEventLogger {
    private static final Logger logger = (Logger) LoggerFactory.getLogger("slack-event-logger");

    public static void logInfo(String text) {
        logger.info(text);
    }
}
