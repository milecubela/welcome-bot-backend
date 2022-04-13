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
            String message = "Tarik Arnaut is one of the three backend developers that made me alive and do all this stuff!" +
                    "He is a Business ICT student and a programming demonstrator at his university" +
                    "He is into IT stuff since he was young, and now he loves programming and building apps." +
                    "Tarik implemented all the Slack API integration with event listeners, event handlers and slash commands." +
                    "Also, he did his part on the API creation, testing and overall code design.";
            SlackEventLogger.logInfo("Command /tarik invoked");
            return ctx.ack(message);
        });
    }
}
