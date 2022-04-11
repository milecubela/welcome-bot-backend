package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackCommandsCache;
import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackEventsCache;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SlackService {

    private final Credentials _credentials;
    private final App _app;
    private final SlackEventsCache slackEventsCache;
    private final SlackCommandsCache slackCommandsCache;

    public SlackService(Credentials credentials, App app, SlackEventsCache slackEventsFactory, SlackCommandsCache slackCommandsFactory) {
        _credentials = credentials;
        _app = app;
        slackEventsCache = slackEventsFactory;
        slackCommandsCache = slackCommandsFactory;
    }

    public void subscribeAll() {
        subscribeToEvents();
        subscribeToCommands();
    }

    private void subscribeToEvents() {
        var list = Arrays.stream(TriggerEvent.values()).toList();
        for (TriggerEvent triggerEvent : list) {
            slackEventsCache.get(triggerEvent).subscribeToEvent(_app, _credentials);
        }
    }

    private void subscribeToCommands() {
        var list = Arrays.stream(SlackCommand.values()).toList();
        for (SlackCommand slackCommand : list) {
            slackCommandsCache.get(slackCommand).subscribeToSlackCommand(_app, _credentials);
        }
    }

    public void postMessage(String channel, String text) throws SlackApiException, IOException {
        _app.client().chatPostMessage(r -> r
                .channel(channel)
                .token(_credentials.getSlackBotToken())
                .text(text));
    }

    public void postMessageWithAttachment(String channel, List<Attachment> attachments) throws SlackApiException, IOException {
        _app.client().chatPostMessage(r -> r
                .channel(channel)
                .token(_credentials.getSlackBotToken())
                .attachments(attachments));
    }

}
