package com.nsoft.welcomebot.SlackModule.SlackCommands;

import com.nsoft.welcomebot.SlackModule.SlackInterfaces.SlackCommandsInterface;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.JSON;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;
import com.slack.api.bolt.response.Response;
import org.springframework.stereotype.Component;

@Component
public class SlackWeatherCommand implements SlackCommandsInterface {

    @Override
    public SlackCommand getCommandType() {
        return SlackCommand.WEATHER;
    }

    @Override
    public void subscribeToSlackCommand(App app, Credentials crd) {
        app.command("/weather", (req, ctx) -> {
            var payload = req.getPayload();
            var city = payload.getText();
            var json = JSON.get("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + crd.getOpenWeatherApiKey());
            var sky = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").toString();
            sky = sky.substring(1, sky.length() - 1);
            var temperature = json.getAsJsonObject("main").get("temp").getAsInt() - 273;
            double wind = json.getAsJsonObject("wind").get("speed").getAsInt() * 1.6;
            System.out.println(sky);
            if (city.equals("Mostar")) {
                ctx.say("You live there. Just look outside the window");
                return Response.ok();
            }
            String message = ":thermometer: Weather forecast for " + city + " today is:\n " +
                    "Sky: " + sky +
                    "\nTemperature: " + temperature + "°C" +
                    "\nThe wind is blowing at " + (int) wind + "km/h";
            ctx.say(message);
            return Response.ok();
        });
    }
}
