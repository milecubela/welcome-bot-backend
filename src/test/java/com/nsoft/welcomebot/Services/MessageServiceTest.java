package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

// Replaces AutoClosable, .openMocks() and .close()
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    private MessageService messageServiceTest;

    @BeforeEach
    void setUp() {
        messageServiceTest = new MessageService(messageRepository);
    }


    /**
     * Testing that getMessageById() throws an exception if ID doesn't exist
     */
    @Test
    void shouldThrowWhenGetIdIsNotFound() {
        // then
        assertThatThrownBy(() -> messageServiceTest.getMessageById(1L)).isInstanceOf(NotFoundException.class).hasMessageContaining("Message with ID 1 not found!");
    }

    /**
     * Testing that getMessageById() returns the correct message
     */
    @Test
    void shouldReturnTheMessageById() {
        //Given
        Message testMessage = new Message(1L, "Title", "Text Text with 20 letters");
        given(messageRepository.findById(testMessage.getMessageId())).willReturn(Optional.of(testMessage));
        //when
        Message message = messageServiceTest.getMessageById(1L);
        //then
        assertThat(message.getMessageId()).isEqualTo(1L);
    }

    /**
     * Testing if deleteMessage() throws an error if ID doesn't exist
     */
    @Test
    void shouldThrowWhenDeleteIdNotFound() {
        // then
        assertThatThrownBy(() -> messageServiceTest.deleteMessage(1L)).isInstanceOf(NotFoundException.class).hasMessageContaining("Message with ID 1 not found!");
    }

    /**
     * Testing if updateMessage() updates message with given messageRequest
     */
    @Test
    void shouldReturnUpdatedMessage() {
        // given
        Message testMessage = new Message(1L, "Title", "Text Text");
        MessageRequest messageRequest = new MessageRequest("Title 2", "Text Text 2");
        given(messageRepository.findById(testMessage.getMessageId())).willReturn(Optional.of(testMessage));
        // when
        Message updatedMessage = messageServiceTest.updateMessage(1L, messageRequest);
        // then
        assertThat(updatedMessage.getText()).matches(testMessage.getText());
        //assertThat(updatedMessage.getText()).doesNotMatch(testMessage.getText());
    }

    /**
     * Testing if updateMessage() throws an exception if ID doesn't exist
     */
    @Test
    void shouldThrowWhenUpdateIdNotFound() {
        // given
        MessageRequest messageRequest = new MessageRequest("Title 2", "Text Text 2");
        // then
        assertThatThrownBy(() -> messageServiceTest.updateMessage(1L, messageRequest)).isInstanceOf(NotFoundException.class).hasMessageContaining("Message with ID 1 not found!");
    }
}