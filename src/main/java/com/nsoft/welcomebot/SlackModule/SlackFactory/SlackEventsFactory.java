package com.nsoft.welcomebot.SlackModule.SlackFactory;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackEventInterface;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackEventsFactory {

    private final List<SlackEventInterface> _slackEvents;

    private final Map<TriggerEvent, SlackEventInterface> slackEventsCache = new EnumMap<>(TriggerEvent.class);

    @Autowired
    public SlackEventsFactory(List<SlackEventInterface> slackEvents) {
        _slackEvents = slackEvents;
    }

    public SlackEventInterface get(TriggerEvent triggerEvent) {
        SlackEventInterface event = slackEventsCache.get(triggerEvent);
        if (event == null) {
            add(triggerEvent);
            event = slackEventsCache.get(triggerEvent);
        }
        return event;
    }

    private void add(TriggerEvent triggerEvent) {
        for (SlackEventInterface event : _slackEvents) {
            if (triggerEvent == event.getEventType()) {
                slackEventsCache.put(event.getEventType(), event);
            }
        }
    }

}
