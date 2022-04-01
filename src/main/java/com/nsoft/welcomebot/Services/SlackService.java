package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackCommmandsFactory;
import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackEventsFactory;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;

import java.io.IOException;
import java.util.Arrays;

public class SlackService {

    private final Credentials _credentials;
    private final App _app;
    private final SlackEventsFactory _slackEventsFactory;
    private final SlackCommmandsFactory _slackCommandsFactory;

    public SlackService(Credentials credentials, App app, SlackEventsFactory slackEventsFactory, SlackCommmandsFactory slackCommandsFactory) {
        _credentials = credentials;
        _app = app;
        _slackEventsFactory = slackEventsFactory;
        _slackCommandsFactory = slackCommandsFactory;
    }

    public void subscribeAll() {
        subscribeToEvents();
        subscribeToCommands();
    }

    private void subscribeToEvents() {
        var list = Arrays.stream(TriggerEvent.values()).toList();
        for (TriggerEvent triggerEvent : list) {
            _slackEventsFactory.getEvent(triggerEvent).subscribeToEvent(_app, _credentials);
        }
    }

    private void subscribeToCommands() {
        var list = Arrays.stream(SlackCommand.values()).toList();
        for (SlackCommand slackCommand : list) {
            _slackCommandsFactory.getCommand(slackCommand).subscribeToSlackCommand(_app, _credentials);
        }
    }

    public void postMessage(String channel, String text) throws SlackApiException, IOException {
        _app.client().chatPostMessage(r -> r
                .channel(channel)
                .token(_credentials.getSlackBotToken())
                .text(text));
    }

}
