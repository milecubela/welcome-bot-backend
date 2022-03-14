package com.nsoft.welcomebot.SlackModule;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.event.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
public class SlackModel {

    private App app;
    private List<Trigger> appMentionEventTriggers=new ArrayList<>();
    private List<Trigger> userLeftChannelTriggers=new ArrayList<>();
    private List<Trigger> userJoinedChannelTriggers=new ArrayList<>();


    @Autowired
    private final TriggerRepository _triggerRepository;

    public SlackModel(TriggerRepository triggerRepository){
        _triggerRepository = triggerRepository;
    }

    @Bean
    public App initSlackApp(){
        app=new App();

        app.message("hello", (req, ctx) -> {
            var logger = ctx.logger;
            try {
                var event = req.getEvent();
                // Call the chat.postMessage method using the built-in WebClient
                var result = ctx.client().chatPostMessage(r -> r
                        // Payload message should be posted in the channel where original message was heard
                        .channel(event.getChannel())
                        .text("hello")
                );
                logger.info("result: {}", result);
            } catch (IOException | SlackApiException e) {
                logger.error("error: {}", e.getMessage(), e);
            }
            return ctx.ack();
        });

        app.command("/channel-info", (req, ctx) -> {
            var payload = req.getPayload();
            var convMembers = app.client().conversationsMembers( r -> r
                    .token(System.getenv("SLACK_BOT_TOKEN"))
                    .channel(payload.getChannelId())).getMembers();
            String message = "This is channel about!\nChannel members: ";
            for(int i=0; i<convMembers.size();i++){
                message+="<@"+convMembers.get(i)+">";
                message += (i+1 < convMembers.size()? ", ": ".");
            }
            return ctx.ack(message);
        });

        addTriggers();

        return app;
    }

    public void addTriggers(){
        clearTriggers();
        for(Trigger trigger:_triggerRepository.findAll()){
            if(trigger.getTriggerEvent()== TriggerEvent.APP_MENTION_EVENT){
                appMentionEventTriggers.add(trigger);
            }else if(trigger.getTriggerEvent()==TriggerEvent.CHANNEL_LEFT){
                userLeftChannelTriggers.add(trigger);
            }else if(trigger.getTriggerEvent()==TriggerEvent.CHANNEL_JOINED){
                userJoinedChannelTriggers.add(trigger);
            }
        }
        subscribeToSlackEvents();
    }

    private void subscribeToSlackEvents(){
        subscribeAppMentionEvent(appMentionEventTriggers);
        subscribeUserLeftChannelEvent(userLeftChannelTriggers);
        subscribeUserJoinedChannelEvent(userJoinedChannelTriggers);
    }

    private void clearTriggers(){
        appMentionEventTriggers.clear();
        userLeftChannelTriggers.clear();
        userJoinedChannelTriggers.clear();
    }

    public void subscribeAppMentionEvent(List<Trigger> triggers){
        app.event(AppMentionEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            var channelResult = app.getClient().conversationsInfo( r -> r
                    .token(System.getenv("SLACK_BOT_TOKEN"))
                    .channel(event.getChannel()));
            var channelName=channelResult.getChannel().getName();
            for(Trigger trigger:triggers){
                if(channelName.equals(trigger.getChannel())){
                    ctx.client().chatPostMessage(r -> r
                            // Payload message should be posted in the channel where original message was heard
                            .channel(event.getChannel())
                            .text(trigger.getMessage().getText())
                    );
                }
            }
            return ctx.ack();
        });
    }

    public void subscribeUserLeftChannelEvent(List<Trigger> triggers){
        app.event(MemberLeftChannelEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            for(Trigger trigger:triggers){
                if(event.getChannel().equals(trigger.getChannel())){
                    ctx.client().chatPostMessage(r -> r
                            // Payload message should be posted in the channel where original message was heard
                            .channel(event.getChannel())
                            .text(trigger.getMessage().getText() + "<@"+event.getUser()+">")
                    );
                }
            }
            return ctx.ack();
        });
    }

    public void subscribeUserJoinedChannelEvent(List<Trigger> triggers){
        app.event(MemberJoinedChannelEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            for(Trigger trigger:triggers){
                if(event.getChannel().equals(trigger.getChannel())){
                    ctx.client().chatPostMessage(r -> r
                            // Payload message should be posted in the channel where original message was heard
                            .channel(event.getChannel())
                            .text(trigger.getMessage().getText() + "<@"+event.getUser()+">")
                    );
                }
            }
            return ctx.ack();
        });
    }
}

