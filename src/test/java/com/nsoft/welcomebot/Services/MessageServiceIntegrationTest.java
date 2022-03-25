package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MessageServiceIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(messageRepository);
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    /**
     * Testing if the repository returns all items from database
     * */
    @Test
    void canGetAllMessages() {
        // given
        Message message1 = new Message("Title1", "Text Text1 dvadeset slova");
        Message message2 = new Message("Title2", "Text Text2 dvadeset slova");
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
    void canAddMessage() {
        //given
        MessageRequest messageRequest = new MessageRequest("Title", "Text Text with 20 letters");
        //when
        messageService.createNewMessage(messageRequest);
        //then
        List<Message> messages = messageRepository.findAll();
//        Message message = messageRepositoryMemoryDatabase.getById(1L);
        assertThat(messages.get(0).getText()).matches("Text Text with 20 letters");
    }
}
