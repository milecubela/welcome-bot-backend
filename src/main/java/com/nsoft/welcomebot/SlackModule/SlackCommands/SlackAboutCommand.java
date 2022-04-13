package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.Logger.SlackEventLogger;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;
import org.springframework.stereotype.Component;

@Component
public class SlackAboutCommand implements SlackCommandsInterface {
    @Override
    public SlackCommand getCommandType() {
        return SlackCommand.ABOUT;
    }

    @Override
    public void subscribeToSlackCommand(App app, Credentials crd) {
        String gitLink = "<https://github.com/milecubela/welcome-bot-backend|GitHub repo>";
        app.command("/about", (req, ctx) -> {
            String message = "Hi, I'm a Slack bot made to welcome new members to a workspace and show them around :hugging_face:.  I was made by the Welcome-Bot team consisting of 5 developers, you can see more about them using the following commands :\n" +
                    "/salko, /selma, /tarik, /mile, /nikola.\n" +
                    "Being developed by such a team you'd think I'd have many talents, but actually I only have 3 : Reacting to events , Scheduling messages for you, Sending animated images of cats.\n" +
                    "My code can be found at my " + gitLink + ".";
            SlackEventLogger.logInfo("Command /about invoked");
            return ctx.ack(message);
        });

    }


}
