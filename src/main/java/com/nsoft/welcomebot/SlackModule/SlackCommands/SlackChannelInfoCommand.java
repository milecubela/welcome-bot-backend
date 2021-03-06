package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.Logger.SlackEventLogger;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;
import org.springframework.stereotype.Component;

@Component
public class SlackChannelInfoCommand implements SlackCommandsInterface {

    @Override
    public SlackCommand getCommandType() {
        return SlackCommand.CHANNEL_INFO;
    }

    public void subscribeToSlackCommand(App app, Credentials crd) {
        app.command("/channel-info", (req, ctx) -> {
            var payload = req.getPayload();
            var convMembers = app.client().conversationsMembers(r -> r
                    .token(crd.getSlackBotToken())
                    .channel(payload.getChannelId())).getMembers();
            String message = "This is channel about!\nChannel members: ";
            for (int i = 0; i < convMembers.size(); i++) {
                message += "<@" + convMembers.get(i) + ">";
                message += (i + 1 < convMembers.size() ? ", " : ".");
            }
            SlackEventLogger.logInfo("Command /channel-info invoked");
            return ctx.ack(message);
        });
    }
}
