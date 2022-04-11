package com.nsoft.welcomebot.SlackModule.SlackFactory;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackEventsCache {

    private final List<SlackEventInterface> slackEvents;

    private final Map<TriggerEvent, SlackEventInterface> eventsCache = new EnumMap<>(TriggerEvent.class);

    @Autowired
    public SlackEventsCache(List<SlackEventInterface> slackEvents) {
        this.slackEvents = slackEvents;
    }

    @PostConstruct
    private void initSlackEventsCache() {
        for (SlackEventInterface event : slackEvents) {
            eventsCache.put(event.getEventType(), event);
        }
    }

    public SlackEventInterface get(TriggerEvent triggerEvent) {
        SlackEventInterface event = eventsCache.get(triggerEvent);
        if (event == null) {
            throw new RuntimeException("Unknown command type: " + triggerEvent);
        }
        return event;
    }

}
