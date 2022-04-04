package com.nsoft.welcomebot.SlackModule.SlackEvents;

import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Services.SlackService;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.model.event.DndUpdatedUserEvent;
import org.springframework.stereotype.Component;

@Component
public class SlackDoNotDisturbUpdateEvent implements SlackEventInterface {

    private SlackService slackService;
    private TriggerRepository triggerRepository;

    public SlackDoNotDisturbUpdateEvent(SlackService slackService, TriggerRepository triggerRepository) {
        this.slackService = slackService;
        this.triggerRepository = triggerRepository;
    }

    @Override
    public TriggerEvent getEventType() {
        return TriggerEvent.DND_UPDATED_USER;
    }

    @Override
    public void subscribeToEvent(App app, Credentials crd) {
        app.event(DndUpdatedUserEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            if (event.getDndStatus().isDndEnabled()) {
                String message = "User @" + event.getUser() + " has gone for his lunch break";
                slackService.postMessage("C037FSVSEJX", message);
            } else {
                String message = "User @" + event.getUser() + " returned from his lunch break";
                slackService.postMessage("C037FSVSEJX", message);
            }
            return ctx.ack();
        });
    }
}
