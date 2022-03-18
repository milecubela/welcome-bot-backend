package com.nsoft.welcomebot.SlackModule;

import com.slack.api.Slack;
import com.slack.api.model.event.UserTypingEvent;
import com.slack.api.rtm.RTMClient;
import com.slack.api.rtm.RTMEventHandler;
import com.slack.api.rtm.RTMEventsDispatcher;
import com.slack.api.rtm.RTMEventsDispatcherFactory;
import com.slack.api.rtm.message.Message;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;

@Component
public final class SlackModel {

    RTMEventsDispatcher dispatcher=RTMEventsDispatcherFactory.getInstance();
    String botToken = Token.SLACK_BOT_TOKEN;
    Slack slack = Slack.getInstance();
    RTMClient rtm = slack.rtmConnect(botToken);


    public SlackModel() throws IOException, DeploymentException {
        run();
        addUserTypingEvent("#test", ":wave: Test test :D");
    }

    public void run() throws IOException, DeploymentException {
        rtm.connect();
        rtm.addMessageHandler(dispatcher.toMessageHandler());
     }

     public void addUserTypingEvent(String channelId, String txt){
         RTMEventHandler<UserTypingEvent> userTyping = new RTMEventHandler<UserTypingEvent>() {
             @Override
             public void handle(UserTypingEvent event) {
                 String _channelId = channelId;
                 String _message = Message.builder().channel(_channelId).text(txt).build().toJSONString();
                 rtm.sendMessage(_message);

             }
         };
         dispatcher.register(userTyping);
     }

    public void sendScheduledMessage(String text){
         String _channelId = "C035W3Q5YQK";
         String _message = Message.builder().channel(_channelId).text(text).build().toJSONString();
         rtm.sendMessage(_message);
     }

}
