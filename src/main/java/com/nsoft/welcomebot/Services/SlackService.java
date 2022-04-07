package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackCommmandsFactory;
import com.nsoft.welcomebot.SlackModule.SlackFactory.SlackEventsFactory;
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

    private final Credentials credentials;
    private final App app;
    private final SlackEventsFactory slackEventsFactory;
    private final SlackCommmandsFactory slackCommandsFactory;

    public SlackService(Credentials credentials, App app, SlackEventsFactory slackEventsFactory, SlackCommmandsFactory slackCommandsFactory) {
        this.credentials = credentials;
        this.app = app;
        this.slackEventsFactory = slackEventsFactory;
        this.slackCommandsFactory = slackCommandsFactory;
    }

    public void subscribeAll() {
        subscribeToEvents();
        subscribeToCommands();
    }

    private void subscribeToEvents() {
        var list = Arrays.stream(TriggerEvent.values()).toList();
        for (TriggerEvent triggerEvent : list) {
            slackEventsFactory.getEvent(triggerEvent).subscribeToEvent(app, credentials);
        }
    }

    private void subscribeToCommands() {
        var list = Arrays.stream(SlackCommand.values()).toList();
        for (SlackCommand slackCommand : list) {
            slackCommandsFactory.getCommand(slackCommand).subscribeToSlackCommand(app, credentials);
        }
    }

    public void postMessage(String channel, String text) throws SlackApiException, IOException {
        app.client().chatPostMessage(r -> r
                .channel(channel)
                .token(credentials.getSlackBotToken())
                .text(text));
    }

    public void postMessageWithAttachment(String channel, List<Attachment> attachments) throws SlackApiException, IOException {
        app.client().chatPostMessage(r -> r
                .channel(channel)
                .token(credentials.getSlackBotToken())
                .attachments(attachments));
    }

}
