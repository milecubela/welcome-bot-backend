package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class TriggerServiceUnitTest {

    private TriggerService triggerService;
    @Mock
    private TriggerRepository triggerRepository;
    @Mock
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        triggerService = new TriggerService(triggerRepository, messageRepository);
    }

    @Test
    void shouldThrowOnCreateNewTrigger() {
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
        assertThatThrownBy(() -> triggerService.createNewTrigger(triggerRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowOnGetTriggerById() {
        // given
        Long triggerId = 1L;
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> triggerService.getTriggerById(triggerId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowOnDeleteTrigger() {
        // given
        Long triggerId = 1L;
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> triggerService.deleteTrigger(triggerId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowWhenMessageDoesNotExistsOnUpdateTrigger() {
        // given
        Long messageId = 1L;
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                messageId,
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        given(messageRepository.findById(messageId))
                .willReturn(Optional.empty());
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.of(trigger));
        // when
        // then
        assertThatThrownBy(() -> triggerService.updateTrigger(triggerId, triggerRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Message with ID " + messageId + " not found!");
    }

    @Test
    void shouldThrowWhenTriggerDoesNotExistsOnUpdateTrigger() {
        // given
        Long triggerId = 1L;
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                1L,
                TriggerEvent.APP_MENTION_EVENT
        );
        given(triggerRepository.findById(triggerId))
                .willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> triggerService.updateTrigger(triggerId, triggerRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Trigger with ID " + triggerId + " not found!");
    }

}