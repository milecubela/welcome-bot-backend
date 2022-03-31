package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MessageServiceIntegrationTest {

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
     * Testing if the method returns all items from database
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canGetAllMessages() {
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
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canCreateMessage() {
        //given
        MessageRequest messageRequest = new MessageRequest("Title", "Text Text with 20 letters");
        //when
        messageService.createNewMessage(messageRequest);
        //then
//        List<Message> messages = messageRepository.findAll();
        Message message = messageRepository.getById(1L);
        assertThat(message.getMessageId()).isEqualTo(1L);
    }

    /**
     * Testing if the method returns the correct Message from database
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void cenGetASingleMessageById() {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        //when
        Message receivedMessage = messageService.getMessageById(1L);
        //then
        assertThat(receivedMessage.getMessageId()).isEqualTo(1L);
    }

    /**
     * Testing if the method deletes the correct Message from database
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canDeleteAMessageById() {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        //when
        messageService.deleteMessage(1L);
        //then
        assertThat(messageRepository.findAll().size()).isZero();
    }

    /**
     * Testing if the method updates the Message in database with given MessageRequest
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canUpdateMessageById() {
        //Given
        Message message = new Message("Title", "Text Text with 20 letters");
        MessageRequest messageRequest = new MessageRequest("Title update", "Updated Text Text with 20 letters");
        messageRepository.save(message);
        //when
        Message updatedMessage = messageService.updateMessage(1L, messageRequest);
        //then
        assertThat(updatedMessage.getText()).matches(messageRequest.getText());
    }

    /**
     * Testing if the method returns the proper pagination
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canReturnMessagesByPage() {
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
        assertThat(messagePage.getContent()).isEqualTo(messages);
    }
}
