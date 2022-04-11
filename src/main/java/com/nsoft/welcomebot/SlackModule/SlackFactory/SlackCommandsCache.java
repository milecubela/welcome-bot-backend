package com.nsoft.welcomebot.SlackModule.SlackFactory;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackCommandsCache {

    private final List<SlackCommandsInterface> slackCommands;

    private final Map<SlackCommand, SlackCommandsInterface> commandsCache = new EnumMap<>(SlackCommand.class);

    @Autowired
    public SlackCommandsCache(List<SlackCommandsInterface> slackCommands) {
        this.slackCommands = slackCommands;
    }

    @PostConstruct
    private void initSlackEventsCache() {
        for (SlackCommandsInterface command : slackCommands) {
            commandsCache.put(command.getCommandType(), command);
        }
    }

    public SlackCommandsInterface get(SlackCommand slackCommand) {
        SlackCommandsInterface command = commandsCache.get(slackCommand);
        if (command == null) {
            throw new RuntimeException("Unknown command type: " + slackCommand);
        }
        return command;
    }

}
