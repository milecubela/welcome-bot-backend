package com.nsoft.welcomebot.SlackModule.SlackEvents;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.SlackModule.Logger.SlackEventLogger;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.model.event.AppMentionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class SlackAppMention implements SlackEventInterface {

    private final TriggerRepository triggerRepository;

    @Autowired
    public SlackAppMention(TriggerRepository triggerRepository) {
        this.triggerRepository = triggerRepository;
    }

    @Override
    public TriggerEvent getEventType() {
        return TriggerEvent.APP_MENTION_EVENT;
    }

    @Override
    public void subscribeToEvent(App app, Credentials crd) {
        app.event(AppMentionEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            var channelResult = app.getClient().conversationsInfo(r -> r
                    .token(crd.getSlackBotToken())
                    .channel(event.getChannel()));
            var user = "<@" + event.getUser() + ">";
            var channelName = channelResult.getChannel().getName();
            for (Trigger trigger : triggerRepository.findTriggersByChannelAndIsActiveAndTriggerEvent(channelName, true, getEventType())) {
                String message = MessageFormat.format(trigger.getMessage().getText(), user);
                var result = ctx.say(message);
                SlackEventLogger.logInfo("Message {" + result.getMessage().getText() + "} posted in channel with id " + trigger.getChannel());
            }
            return ctx.ack();
        });
    }
}
