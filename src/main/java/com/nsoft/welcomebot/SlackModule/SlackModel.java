package com.nsoft.welcomebot.SlackModule;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import com.slack.api.model.event.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebMvc
public class SlackModel {

    private App app;
    private List<Trigger> appMentionEventTriggers;
    private List<Trigger> userLeftChannelTriggers;

    @Autowired
    private final TriggerRepository _triggerRepository;

    public SlackModel(List<Trigger> appMentionEventTriggers, List<Trigger> userLeftChannelTriggers, TriggerRepository triggerRepository) throws Exception {
        this.appMentionEventTriggers = appMentionEventTriggers;
        this.userLeftChannelTriggers = userLeftChannelTriggers;
        _triggerRepository = triggerRepository;
    }

    @Bean
    public App initSlackApp() throws Exception {
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

        addTriggers();

        return app;
    }

    public void addTriggers() throws SlackApiException, IOException {
        for(Trigger trigger:_triggerRepository.findAll()){
            if(trigger.getTriggerEvent()== TriggerEvent.APP_MENTION_EVENT){
                appMentionEventTriggers.add(trigger);
            }else if(trigger.getTriggerEvent()==TriggerEvent.CHANNEL_LEFT){
                userLeftChannelTriggers.add(trigger);
            }
        }
        addAppMentionEvent(appMentionEventTriggers);
        addUserLeftChannelEvent(userLeftChannelTriggers);
    }

    public void addAppMentionEvent(List<Trigger> triggers){
        app.event(AppMentionEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            var channelResult = app.getClient().conversationsInfo( r -> r
                    .token(System.getenv("SLACK_BOT_TOKEN"))
                    .channel(event.getChannel()));
            var channelName=channelResult.getChannel().getName();
            for(Trigger trigger:triggers){
                if(channelName.equals(trigger.getChannel())){
                    var result = ctx.client().chatPostMessage(r -> r
                            // Payload message should be posted in the channel where original message was heard
                            .channel(event.getChannel())
                            .text(trigger.getMessage().getText())
                    );
                }
            }
            return ctx.ack();
        });
    }

    public void addUserLeftChannelEvent(List<Trigger> triggers) throws SlackApiException, IOException {
        app.event(MemberLeftChannelEvent.class, (payload, ctx) -> {
            var event = payload.getEvent();
            for(Trigger trigger:triggers){
                if(event.getChannel().equals(trigger.getChannel())){
                    var result = ctx.client().chatPostMessage(r -> r
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

