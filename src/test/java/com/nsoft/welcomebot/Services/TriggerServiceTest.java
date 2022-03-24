package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class TriggerServiceTest {

    private TriggerService triggerServiceTest;
    @Mock
    private TriggerRepository triggerRepository;
    @Mock
    private MessageRepository messageRepository;
    @Autowired
    private TriggerRepository triggerRepositoryMemoryBase;
    @Autowired
    private MessageRepository messageRepositoryMemoryBase;

    @BeforeEach
    void setUp() {
        triggerServiceTest = new TriggerService(triggerRepository, messageRepository);
    }

    @Test
    void canCreateNewTrigger() {
        // given
        // initializing triggerServiceTest to use the trigger repository that is on the H2 memory base
        // so we can see if it is actually stored.
        triggerServiceTest = new TriggerService(triggerRepositoryMemoryBase, messageRepositoryMemoryBase);
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        messageRepositoryMemoryBase.save(message);
        // when
        triggerServiceTest.createNewTrigger(triggerRequest);
        // then
        boolean expected = triggerRepositoryMemoryBase.findAll().size() > 0;
        assertThat(expected).isTrue();
    }

    @Test
    void willThrowOnCreateNewTrigger() {
        // given
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        given(messageRepository.findById(triggerRequest.getMessageId()))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> triggerServiceTest.createNewTrigger(triggerRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void canGetTriggerById() {
        // given
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        given(triggerRepository.findById(trigger.getTriggerId()))
                .willReturn(Optional.of(trigger));
        // when
        Trigger triggerReturn = triggerServiceTest.getTriggerById(trigger.getTriggerId());
        // then
        assertThat(trigger).isEqualTo(triggerReturn);
    }

    @Test
    void willThrowOnGetTriggerById() {
        // given
        Long triggerId = 1L;
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> triggerServiceTest.getTriggerById(triggerId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void canDeleteTrigger() {
        // given
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.of(trigger));
        // when
        triggerServiceTest.deleteTrigger(triggerId);
        // then
        verify(triggerRepository).deleteById(triggerId);
    }

    @Test
    void willThrowOnDeleteTrigger() {
        // given
        Long triggerId = 1L;
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> triggerServiceTest.deleteTrigger(triggerId))
                .isInstanceOf(NotFoundException.class);
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
        message.setMessageId(1L);
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        TriggerRequest triggerRequestUpdate = new TriggerRequest(
                "channelUpdated",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        given(messageRepository.findById(message.getMessageId()))
                .willReturn(Optional.of(message));
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.of(trigger));
        // when
        Trigger returnTrigger = triggerServiceTest.updateTrigger(triggerId, triggerRequestUpdate);
        // then
        assertThat(returnTrigger.getChannel()).doesNotMatch(triggerRequest.getChannel());
    }

    @Test
    void willThrowWhenMessageDoesNotExistsOnUpdateTrigger() {
        // given
        Long messageId = 1L;
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        given(messageRepository.findById(messageId))
                .willReturn(Optional.empty());
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.of(trigger));
        // when
        // then
        assertThatThrownBy(() -> triggerServiceTest.updateTrigger(triggerId, triggerRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Message with ID " + messageId + " not found!");
    }

    @Test
    void willThrowWhenTriggerDoesNotExistsOnUpdateTrigger() {
        // given
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                triggerId,
                TriggerEvent.APP_MENTION_EVENT
        );
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> triggerServiceTest.updateTrigger(triggerId, triggerRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Trigger with ID " + triggerId + " not found!");
    }

}