package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.Logger.SlackEventLogger;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;
import org.springframework.stereotype.Component;

@Component
public class SlackMileCommand implements SlackCommandsInterface {
    @Override
    public SlackCommand getCommandType() {
        return SlackCommand.MILE;
    }

    @Override
    public void subscribeToSlackCommand(App app, Credentials crd) {
        app.command("/mile", (req, ctx) -> {
            String message = "Mile is 24 years old, and comes from Mostar. His passion for IT started when he was very young," +
                    " and was ignited by gaming. In middle school, he hosted game servers which even made him some allowance money." +
                    " He went to college for a short time, then dropped out. He replaced college with a game development startup which he founded" +
                    " with few of his friends, here at Spark. Now, he is dedicated to his home-learning software development career, " +
                    "and is passionate about US Stock Market and investing.\n" +
                    "Mile is responsible for my security, by implementing OAuth Security for my backend application. " +
                    "He also worked on database design, API design and development, and overall application testing :)";
            SlackEventLogger.logInfo("Command /mile invoked");
            return ctx.ack(message);
        });
    }
}
