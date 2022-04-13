package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.Logger.SlackEventLogger;
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
            String message = "Nikola is a 24yr old student from Mostar, ever since a young age he's been passionate about technology " +
                    "and he decided to follow that passion. He's currently attending the Faculty of Information Technologies" +
                    " where he learned a wide variety of soft and technical skills, and he's hoping to add more." +
                    " Some of his interests are technology, mountain biking and cryptocurrencies.\n" +
                    "Nikola implemented my scheduled messages and gifs feature and dockerized me so I can be portable :pinching_hand:." +
                    " He also worked on my API counterpart and wrote my tests.";
            SlackEventLogger.logInfo("Command /nikola invoked");
            return ctx.ack(message);
        });
    }
}
