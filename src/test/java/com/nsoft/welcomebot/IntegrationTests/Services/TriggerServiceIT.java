package com.nsoft.welcomebot.IntegrationTests.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Services.TriggerService;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
class TriggerServiceIT {

    private TriggerService triggerService;
    @Autowired
    public TriggerRepository triggerRepository;
    @Autowired
    public MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        triggerService = new TriggerService(triggerRepository, messageRepository);
    }

    @BeforeEach
    void clean() {
        triggerRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void shouldCreateNewTrigger() {
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        message = messageRepository.findAll().get(0);
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                message.getMessageId(),
                TriggerEvent.APP_MENTION_EVENT
        );
        // when
        triggerService.createNewTrigger(triggerRequest);
        // then
        var a = triggerRepository.findAll();
        Integer expected = triggerRepository.findAll().size();
        assertThat(expected).isEqualTo(1);
    }

    @Test
    void shouldGetTriggerById() {
        // given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        Long messageId = messageRepository.findAll().get(0).getMessageId();
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                messageId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        trigger.setMessage(message);
        triggerRepository.save(trigger);
        // when
        Trigger triggerReturn = triggerService.getTriggerById(trigger.getTriggerId());
        // then
        assertThat(trigger.getChannel()).isEqualTo(triggerReturn.getChannel());
    }

    @Test
    void shouldDeleteTrigger() {
        // given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        Long messageId = messageRepository.findAll().get(0).getMessageId();
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                messageId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        trigger.setMessage(message);
        triggerRepository.save(trigger);
        Long triggerId = triggerRepository.findAll().get(0).getTriggerId();
        // when
        triggerService.deleteTrigger(triggerId);
        // then
        assertThat(triggerRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void shouldUpdateTrigger() {
        // given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        Long messageId = messageRepository.findAll().get(0).getMessageId();
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                messageId,
                TriggerEvent.APP_MENTION_EVENT
        );
        TriggerRequest triggerRequestUpdate = new TriggerRequest(
                "channelUpdated",
                true,
                messageId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        trigger.setMessage(message);
        triggerRepository.save(trigger);
        Trigger finalTrigger = triggerRepository.findAll().get(0);
        // when
        Trigger returnTrigger = triggerService.updateTrigger(finalTrigger.getTriggerId(), triggerRequestUpdate);
        // then
        assertThat(returnTrigger.getChannel()).doesNotMatch(triggerRequest.getChannel());
    }
}
