package com.nsoft.welcomebot.SlackModule.SlackFactory;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackCommmandsFactory {

    private final List<SlackCommandsInterface> _slackCommands;

    private final Map<SlackCommand, SlackCommandsInterface> slackCommandsCache = new HashMap<>();

    @Autowired
    public SlackCommmandsFactory(List<SlackCommandsInterface> slackCommands) {
        _slackCommands = slackCommands;
    }

    @PostConstruct
    public void initSlackCommandsCache() {
        for (SlackCommandsInterface command : _slackCommands) {
            slackCommandsCache.put(command.getCommandType(), command);
        }
    }

    public SlackCommandsInterface getCommand(SlackCommand commandType) {
        SlackCommandsInterface command = slackCommandsCache.get(commandType);
        if (command == null) throw new RuntimeException("Unknown command: " + commandType);
        return command;
    }

}
