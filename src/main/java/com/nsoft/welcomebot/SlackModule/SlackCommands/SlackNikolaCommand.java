package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;
import org.springframework.stereotype.Component;

@Component
public class SlackNikolaCommand implements SlackCommandsInterface {
    @Override
    public SlackCommand getCommandType() {
        return SlackCommand.NIKOLA;
    }

    @Override
    public void subscribeToSlackCommand(App app, Credentials crd) {
        app.command("/nikola", (req, ctx) -> {
            String message = "Nikola about";
            return ctx.ack(message);
        });
    }
}
