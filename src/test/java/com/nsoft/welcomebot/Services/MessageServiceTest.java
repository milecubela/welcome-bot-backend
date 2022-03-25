package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DataJpaTest
// Replaces AutoClosable, .openMocks() and .close()
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Autowired
    private MessageRepository messageRepositoryMemoryDatabase;
    private MessageService messageServiceTest;

    @BeforeEach
    void setUp() {
        messageServiceTest = new MessageService(messageRepository);
    }

    @AfterEach
    void tearDown() {
        messageRepositoryMemoryDatabase.deleteAll();
    }

    @Test
    void canGetAllMessages() {
        messageServiceTest = new MessageService(messageRepositoryMemoryDatabase);
        // given
        Message message1 = new Message("Title1", "Text Text1 dvadeset slova");
        Message message2 = new Message("Title2", "Text Text2 dvadeset slova");
        messageRepositoryMemoryDatabase.save(message1);
        messageRepositoryMemoryDatabase.save(message2);
        //when
        List<Message> messages = messageServiceTest.getMessages();
        //then
        assertThat(messages.size()).isEqualTo(2);
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
     * Testing if the end result from the repository is the same as the messageRequest we sent to create
     */
    @Test
    void canAddMessage() {
        messageServiceTest = new MessageService(messageRepositoryMemoryDatabase);
        //given
        MessageRequest messageRequest = new MessageRequest("Title", "Text Text with 20 letters");
        //when
        messageServiceTest.createNewMessage(messageRequest);
        //then
        List<Message> messages = messageRepositoryMemoryDatabase.findAll();
//        Message message = messageRepositoryMemoryDatabase.getById(1L);
        assertThat(messages.get(0).getText()).matches("Text Text with 20 letters");
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