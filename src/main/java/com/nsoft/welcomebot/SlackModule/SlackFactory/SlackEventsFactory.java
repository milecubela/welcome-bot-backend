package com.nsoft.welcomebot.SlackModule.SlackFactory;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackEventsFactory {

    private final List<SlackEventInterface> _slackEvents;

    private final Map<TriggerEvent, SlackEventInterface> slackEventsCache = new HashMap<>();

    @Autowired
    public SlackEventsFactory(List<SlackEventInterface> slackEvents) {
        _slackEvents = slackEvents;
    }

    @PostConstruct
    public void initSlackEventsCache() {
        for (SlackEventInterface event : _slackEvents) {
            slackEventsCache.put(event.getEventType(), event);
        }
    }

    public SlackEventInterface getEvent(TriggerEvent type) {
        SlackEventInterface event = slackEventsCache.get(type);
        if (event == null) throw new RuntimeException("Unknown event type: " + type);
        return event;
    }

}
