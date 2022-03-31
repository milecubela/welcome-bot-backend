package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
class TriggerServiceIntegrationTest {

    private TriggerService triggerService;
    @Autowired
    public TriggerRepository triggerRepository;
    @Autowired
    public MessageRepository messageRepository;

    @Container
    public static MariaDBContainer container = (MariaDBContainer) new MariaDBContainer("mariadb")
            .withDatabaseName("wbotTest")
            .withUsername("root")
            .withPassword("");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeEach
    void setUp() {
        triggerService = new TriggerService(triggerRepository, messageRepository);
    }

    @AfterEach
    void clean() {
        triggerRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void canCreateNewTrigger() {
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
        Integer expected = triggerRepository.findAll().size();
        assertThat(expected).isEqualTo(1);
    }

    @Test
    void canGetTriggerById() {
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
    void canDeleteTrigger() {
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
    void canUpdateTrigger() {
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
