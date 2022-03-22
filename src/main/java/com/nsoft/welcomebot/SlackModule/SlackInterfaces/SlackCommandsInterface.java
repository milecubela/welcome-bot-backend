package com.nsoft.welcomebot.SlackModule.SlackInterfaces;

import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SlackCommand;
import com.slack.api.bolt.App;

public interface SlackCommandsInterface {

    SlackCommand getCommandType();

    void subscribeToSlackCommand(App app, Credentials crd);
}
