package com.nsoft.welcomebot.IntegrationTests.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
class MessageServiceIT {

    @Autowired
    private MessageRepository messageRepository;
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(messageRepository);
    }

    @BeforeEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    /**
     * Testing if the method returns all items from database
     */
    @Test
    void shouldGetAllMessages() {
        // given
        Message message1 = new Message("Title1", "Text Text1 with 20 letters");
        Message message2 = new Message("Title2", "Text Text2 with 20 letters");
        messageRepository.save(message1);
        messageRepository.save(message2);
        //when
        List<Message> messages = messageService.getMessages();
        //then
        assertThat(messages.size()).isEqualTo(2);
    }

    /**
     * Testing if the end result from the repository is the same as the messageRequest we sent to create
     */
    @Test
    void shouldCreateMessage() {
        //given
        MessageRequest messageRequest = new MessageRequest("Title", "Text Text with 20 letters");
        //when
        messageService.createNewMessage(messageRequest);
        //then
        Integer size = messageRepository.findAll().size();
        assertThat(size).isEqualTo(1);
    }

    /**
     * Testing if the method returns the correct Message from database
     */
    @Test
    void shouldGetASingleMessageById() {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Long messageId = messageRepository.findAll().get(0).getMessageId();
        //when
        Message receivedMessage = messageService.getMessageById(messageId);
        //then
        assertThat(receivedMessage.getMessageId()).isEqualTo(messageId);
    }

    /**
     * Testing if the method deletes the correct Message from database
     */
    @Test
    void shouldDeleteAMessageById() {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Long messageId = messageRepository.findAll().get(0).getMessageId();
        //when
        messageService.deleteMessage(messageId);
        //then
        assertThat(messageRepository.findAll().size()).isZero();
    }

    /**
     * Testing if the method updates the Message in database with given MessageRequest
     */
    @Test
    void shouldUpdateMessageById() {
        //Given
        Message message = new Message("Title", "Text Text with 20 letters");
        MessageRequest messageRequest = new MessageRequest("Title update", "Updated Text Text with 20 letters");
        messageRepository.save(message);
        //when
        Message updatedMessage = messageService.updateMessage(message.getMessageId(), messageRequest);
        //then
        assertThat(updatedMessage.getText()).matches(messageRequest.getText());
    }

    /**
     * Testing if the method returns the proper pagination
     */
    @Test
    void shouldReturnMessagesByPage() {
        // given
        Message message1 = new Message("Title1", "Text Text1 with 20 letters");
        Message message2 = new Message("Title2", "Text Text2 with 20 letters");
        Message message3 = new Message("Title3", "Text Text3 with 20 letters");
        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);
        List<Message> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        // then
        Page<Message> messagePage = messageService.findAllPaginated(0, 2);
        // then
        assertThat(messagePage.getContent().size()).isEqualTo(messages.size());
    }
}
