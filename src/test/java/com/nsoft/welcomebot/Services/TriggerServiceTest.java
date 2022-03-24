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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TriggerServiceTest {

    private TriggerService triggerServiceTest;
    @Mock
    private TriggerRepository triggerRepository;
    @Mock
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        triggerServiceTest = new TriggerService(triggerRepository, messageRepository);
    }

    @Test
    void canCreateNewTrigger() {
        // given
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
        given(messageRepository.findById(triggerRequest.getMessageId()))
                .willReturn(Optional.of(message));

        // when
        triggerServiceTest.createNewTrigger(triggerRequest);

        // then
        ArgumentCaptor<Trigger> triggerArgumentCaptor =
                ArgumentCaptor.forClass(Trigger.class);
        verify(triggerRepository).save(triggerArgumentCaptor.capture());
        Trigger trigger = triggerArgumentCaptor.getValue();
        boolean expected = trigger.getChannel().matches(triggerRequest.getChannel());
        assertThat(expected).isTrue();
    }

    @Test
    void willThrowCreateNewTrigger() {
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
    void willThrowGetTriggerById() {
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
    void willThrowCanDeleteTrigger() {
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
    void willThrowWhenMessageDoesNotExistsUpdateTrigger() {
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
    void willThrowWhenTriggerDoesNotExistsUpdateTrigger() {
        // given
        Long messageId = 1L;
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