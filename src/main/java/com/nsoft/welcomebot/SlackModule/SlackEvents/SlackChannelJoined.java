package com.nsoft.welcomebot.SlackModule.SlackEvents;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.model.event.MemberJoinedChannelEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlackChannelJoined implements SlackEventInterface {

    private final TriggerRepository _triggerRepository;

    @Autowired
    public SlackChannelJoined(TriggerRepository triggerRepository) {
        _triggerRepository = triggerRepository;
    }

    @Override
    public TriggerEvent getEventType() {
        return TriggerEvent.CHANNEL_JOINED;
    }

    @Override
    public void subscribeToEvent(App app, Credentials crd) {
        app.event(MemberJoinedChannelEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            var channelResult = app.getClient().conversationsInfo(r -> r
                    .token(crd.getSlackBotToken())
                    .channel(event.getChannel()));
            var channelName = channelResult.getChannel().getName();
            for (Trigger trigger : _triggerRepository.findTriggersByChannelAndIsActiveAndTriggerEvent(channelName, true, getEventType())) {
                ctx.say(trigger.getMessage().getText());
            }
            return ctx.ack();
        });
    }
}
