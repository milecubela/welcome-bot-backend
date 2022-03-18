package com.nsoft.welcomebot.SlackModule.SlackEvents;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.model.event.MemberLeftChannelEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlackChannelLeft implements SlackEventInterface {

    private final TriggerRepository _triggerRepository;

    @Autowired
    public SlackChannelLeft(TriggerRepository triggerRepository) {
        _triggerRepository = triggerRepository;
    }

    @Override
    public TriggerEvent getEventType() {
        return TriggerEvent.CHANNEL_LEFT;
    }

    @Override
    public void subscribeToEvent(App app) {
        app.event(MemberLeftChannelEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            var channelResult = app.getClient().conversationsInfo(r -> r
                    .token(System.getenv("SLACK_BOT_TOKEN"))
                    .channel(event.getChannel()));
            var channelName = channelResult.getChannel().getName();
            for (Trigger trigger : _triggerRepository.findTriggersByChannel(channelName)) {
                ctx.say(trigger.getMessage().getText());
            }
            return ctx.ack();
        });
    }
}