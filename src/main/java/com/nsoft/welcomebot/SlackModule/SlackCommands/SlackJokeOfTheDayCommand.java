package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.Logger.SlackEventLogger;
import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.ConsumeJSON;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;
import com.slack.api.bolt.response.Response;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SlackJokeOfTheDayCommand implements SlackCommandsInterface {

    @Override
    public SlackCommand getCommandType() {
        return SlackCommand.JOKE_OF_THE_DAY;
    }

    @Override
    public void subscribeToSlackCommand(App app, Credentials crd) {
        app.command("/joke_of_the_day", (req, ctx) -> {
            var json = ConsumeJSON.getJSONObject("https://api.jokes.one/jod");
            String joke = json.getAsJsonObject("contents").getAsJsonArray("jokes").get(0).getAsJsonObject().getAsJsonObject("joke").get("text").getAsString();
            String message = "Joke of the day for " + LocalDate.now() + " is \n" + joke;
            ctx.say(message);
            SlackEventLogger.logInfo("Command /joke_of_the_day invoked");
            return Response.ok();
        });
    }
}
