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
     * To break the test, uncomment the Message and given() . It makes sure that findById() will return the valid Message object, and therefor not throw an exception
     */
    @Test
    void willThrowWhenGetIdIsNotFound() {
        // given
//        Message message = new Message(1L, "Title", "Text Text");
//        given(messageRepository.findById(message.getMessageId())).willReturn(Optional.of(message));
        // When
        // then
        assertThatThrownBy(() -> messageServiceTest.getMessageById(1L)).isInstanceOf(NotFoundException.class).hasMessageContaining("Message with ID 1 not found!");
    }

    /**
     * To break this test, assert that the text is not equal
     */
    @Test
    void willReturnTheMessageById() {
        //Given
        Message testMessage = new Message(1L, "Title", "Text Text with 20 letters");
        given(messageRepository.findById(testMessage.getMessageId())).willReturn(Optional.of(testMessage));
        //when
        Message message = messageServiceTest.getMessageById(1L);
        //then
        assertThat(message.getText()).matches("Text Text with 20 letters");
        //assertThat(message.getText()).matches("Not the same text");
    }

    /**
     * To break the test, uncomment the Message and given() . It makes sure that findById() will return the valid Message object, and therefor not throw an exception
     */
    @Test
    void willThrowWhenDeleteIdNotFound() {
        // given
//        Message message = new Message(1L, "Title", "Text Text");
//        given(messageRepository.findById(message.getMessageId())).willReturn(Optional.of(message));
        // When
        // then
        assertThatThrownBy(() -> messageServiceTest.deleteMessage(1L)).isInstanceOf(NotFoundException.class).hasMessageContaining("Message with ID 1 not found!");
    }

    /**
     * To break the test, assert that the text is not equal
     */
    @Test
    void willReturnUpdatedMessage() {
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
     * To break the test, uncomment the Message and given() . It makes sure that findById() will return the valid Message object, and therefor not throw an exception
     */
    @Test
    void willThrownWhenUpdateIdNotFound() {
        // given
        MessageRequest messageRequest = new MessageRequest("Title 2", "Text Text 2");
//        Message message = new Message(1L, "Title", "Text Text");
//        given(messageRepository.findById(message.getMessageId())).willReturn(Optional.of(message));
        // When
        // then
        assertThatThrownBy(() -> messageServiceTest.updateMessage(1L, messageRequest)).isInstanceOf(NotFoundException.class).hasMessageContaining("Message with ID 1 not found!");
    }
}