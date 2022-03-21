package com.nsoft.welcomebot.SlackModule.SlackInterfaces;

import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;

public interface SlackEventInterface {

    TriggerEvent getEventType();

    void subscribeToEvent(App app, Credentials crd);
}
