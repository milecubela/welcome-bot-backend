package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackCommmandsFactory;
import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackEventsFactory;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class SlackService {

    private final App _app;
    private final SlackEventsFactory _slackEventsFactory;
    private final SlackCommmandsFactory _slackCommandsFactory;

    @Autowired
    public SlackService(App app, SlackEventsFactory slackEventsFactory, SlackCommmandsFactory slackCommandsFactory) {
        _app = app;
        _slackEventsFactory = slackEventsFactory;
        _slackCommandsFactory = slackCommandsFactory;
        subscribeAll();
    }

    private void subscribeAll() {
        subscribeToEvents();
        subscribeToCommands();
    }

    private void subscribeToEvents() {
        var list = Arrays.stream(TriggerEvent.values()).toList();
        for (TriggerEvent triggerEvent : list) {
            _slackEventsFactory.getEvent(triggerEvent).subscribeToEvent(_app);
        }
    }

    private void subscribeToCommands() {
        var list = Arrays.stream(SlackCommand.values()).toList();
        for (SlackCommand slackCommand : list) {
            _slackCommandsFactory.getCommand(slackCommand).subscribeToSlackCommand(_app);
        }
    }

}
