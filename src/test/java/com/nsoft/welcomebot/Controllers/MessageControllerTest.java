package com.nsoft.welcomebot.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(MessageController.class)
@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    /**
     * Testing if the GET /api/v1/messages will return all messages from database
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canReturnAllMessages() throws Exception {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        Message message2 = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        messageRepository.save(message2);
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/messages"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseBody = result.getResponse().getContentAsString();
        List<Message> messageList = objectMapper.readValue(responseBody, new TypeReference<List<Message>>() {
        });
        assertThat(messageList.size()).isEqualTo(2);
    }

    /**
     * Testing if the GET /api/v1/messages?=offest pagesize return correct pagination
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canReturnPaginatedMessages() throws Exception {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        Message message2 = new Message("Title", "Text Text with 20 letters");
        Message message3 = new Message("Title", "Text Text2 with 20 letters");
        Message message4 = new Message("Title", "Text Text3 with 20 letters");
        messageRepository.save(message);
        messageRepository.save(message2);
        messageRepository.save(message3);
        messageRepository.save(message4);
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/messages?offset=1&pagesize=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("Text Text2 with 20 letters");
        assertThat(responseBody).contains("Text Text3 with 20 letters");
    }

    /**
     * Testing if the GET /api/v1/messages/{messageId} returns a message with that ID
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canReturnASingleMessageById() throws Exception {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/messages/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        String responseBody = result.getResponse().getContentAsString();
        Message responseMessage = objectMapper.readValue(responseBody, Message.class);
        assertThat(responseMessage.getMessageId()).isEqualTo(1L);
    }

    /**
     * Testing if the GET /api/v1/messages/{messageId} returns 404 if message is not found
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnNotFoundIfMessageByIdDoesntExist() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/messages/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * Testing if the GET /api/v1/messages/{messageId} returns 404 if message is not found
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnBadRequestIfPathVariableIsNotValid() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/messages/as"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}