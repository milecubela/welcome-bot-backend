package com.nsoft.welcomebot.SlackModule.SlackEvents;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.model.event.ReactionAddedEvent;
import org.springframework.stereotype.Component;

@Component
public class SlackReactionAddedEvent implements SlackEventInterface {
    @Override
    public TriggerEvent getEventType() {
        return TriggerEvent.REACTION_ADDED;
    }

    @Override
    public void subscribeToEvent(App app, Credentials crd) {
        app.event(ReactionAddedEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            var user = "<@" + event.getUser() + ">";
            var itemUser = "<@" + event.getItemUser() + ">";
            var reaction = event.getReaction();
            String message = "User " + user + " reacted with :" + reaction + ": to " + itemUser + "'s message";
            ctx.setChannelId("C037FSVSEJX");
            ctx.say(message);
            return ctx.ack();
        });
    }
}
