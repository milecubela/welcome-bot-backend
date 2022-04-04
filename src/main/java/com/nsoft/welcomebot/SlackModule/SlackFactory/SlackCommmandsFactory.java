package com.nsoft.welcomebot.SlackModule.SlackFactory;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackCommmandsFactory {

    private final List<SlackCommandsInterface> _slackCommands;

    private final Map<SlackCommand, SlackCommandsInterface> slackCommandsCache = new EnumMap<>(SlackCommand.class);

    @Autowired
    public SlackCommmandsFactory(List<SlackCommandsInterface> slackCommands) {
        _slackCommands = slackCommands;
    }

    public SlackCommandsInterface get(SlackCommand commandType) {
        if (commandType == null) {
            throw new RuntimeException("Unknown command: " + null);
        }
        SlackCommandsInterface event = slackCommandsCache.get(commandType);
        if (event == null) {
            add(commandType);
            event = slackCommandsCache.get(commandType);
        }
        return event;
    }

    private void add(SlackCommand triggerEvent) {
        for (SlackCommandsInterface event : _slackCommands) {
            if (triggerEvent == event.getCommandType()) {
                slackCommandsCache.put(event.getCommandType(), event);
            }
        }
    }

}
