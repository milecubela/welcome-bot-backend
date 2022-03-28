package com.nsoft.welcomebot.Controllers;

import com.google.gson.Gson;
import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(MessageController.class)
@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

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

    // private final MessageController messageController = new MessageController(messageService);

    /**
     * Testing if the GET /api/v1/messages will return all messages from database
     */
    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnAllMessages() throws Exception {
        //given
        MessageRequest messageRequest = new MessageRequest("Title1", "Text Text1 with 20 letters");
        MessageRequest messageRequest2 = new MessageRequest("Title1", "Text Text2 with 20 letters");
        messageService.createNewMessage(messageRequest);
        messageService.createNewMessage(messageRequest2);
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/messages"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("Text Text1 with 20 letters");
        assertThat(responseBody).contains("Text Text2 with 20 letters");
    }

    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnPaginatedMessages() throws Exception {
        //given
        MessageRequest messageRequest = new MessageRequest("Title1", "Text Text1 with 20 letters");
        MessageRequest messageRequest2 = new MessageRequest("Title1", "Text Text2 with 20 letters");
        MessageRequest messageRequest3 = new MessageRequest("Title1", "Text Text3 with 20 letters");
        MessageRequest messageRequest4 = new MessageRequest("Title1", "Text Text4 with 20 letters");
        messageService.createNewMessage(messageRequest);
        messageService.createNewMessage(messageRequest2);
        messageService.createNewMessage(messageRequest3);
        messageService.createNewMessage(messageRequest4);
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/messages?offset=1&pagesize=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("Text Text3 with 20 letters");
        assertThat(responseBody).contains("Text Text4 with 20 letters");
    }
}