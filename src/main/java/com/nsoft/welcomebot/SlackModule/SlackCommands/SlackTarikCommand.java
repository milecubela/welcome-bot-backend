package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.Logger.SlackEventLogger;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;
import org.springframework.stereotype.Component;

@Component
public class SlackTarikCommand implements SlackCommandsInterface {
    @Override
    public SlackCommand getCommandType() {
        return SlackCommand.TARIK;
    }

    @Override
    public void subscribeToSlackCommand(App app, Credentials crd) {
        app.command("/tarik", (req, ctx) -> {
            String message = "Tarik about";
            SlackEventLogger.logInfo("Command /tarik invoked");
            return ctx.ack(message);
        });
    }
}
