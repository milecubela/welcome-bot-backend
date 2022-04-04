package com.nsoft.welcomebot.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(MessageController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
//@ActiveProfiles("h2")
class MessageControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    /**
     * Testing if the GET /api/v1/messages will return all messages from database
     */
    @Test
    void shouldReturnAllMessages() throws Exception {
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
        List<Message> messageList = objectMapper.readValue(responseBody, new TypeReference<>() {
        });
        assertThat(messageList.size()).isEqualTo(2);
    }

    /**
     * Testing if the GET /api/v1/messages?=offset pagesize return correct pagination
     */
    @Test
    void shouldReturnPaginatedMessages() throws Exception {
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
    void shouldReturnASingleMessageById() throws Exception {
        //given
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Long messageId = messageRepository.findAll().get(0).getMessageId();
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/messages/" + messageId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // then
        String responseBody = result.getResponse().getContentAsString();
        Message responseMessage = objectMapper.readValue(responseBody, Message.class);
        assertThat(responseMessage.getMessageId()).isEqualTo(messageId);
    }

    /**
     * Testing if the GET /api/v1/messages/{messageId} returns 404 if message is not found
     */
    @Test
    void shouldReturnNotFoundIfMessageByIdDoesntExist() throws Exception {
        mockMvc.perform(get("/api/v1/messages/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Testing if the GET /api/v1/messages/{messageId} returns 404 if message is not found
     */
    @Test
    void shouldReturnBadRequestIfPathVariableIsNotValid() throws Exception {
        mockMvc.perform(get("/api/v1/messages/as"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Testing if the POST /api/v1/messages returns 201 and creates a new message in database
     */
    @Test
    void shouldCreateANewMessageWithMessageRequest() throws Exception {
        //given
        MessageRequest messageRequest = new MessageRequest("Title", "Text Text with 20 letters");
        String requestJson = objectMapper.writeValueAsString(messageRequest);
        //when
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated());
        List<Message> messages = messageRepository.findAll();
        //then
        assertThat(messages.size()).isEqualTo(1);
    }

    /**
     * Testing if the POST /api/v1/messages returns bad request
     */
    @Test
    void shouldReturnBadRequestIfRequestBodyIsInvalid() throws Exception {
        //given
        // Text should have min 20 characters
        MessageRequest badMessageRequest = new MessageRequest("Title", "Text Text");
        String requestJson = objectMapper.writeValueAsString(badMessageRequest);
        //when
        mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Testing if DELETE /api/v1/messages/{messageId} returns 200 and deletes a message from database
     */
    @Test
    void shouldDeleteAMessageByIdFromDeleteRequest() throws Exception {
        //given
        Message message = new Message(1L, "Title", "Text Text with 20 letters");
        messageRepository.save(message);
        String messageId = messageRepository.findAll().get(0).getMessageId().toString();
        //when
        mockMvc.perform(delete("/api/v1/messages/" + messageId))
                .andExpect(status().isOk());
        //then
        List<Message> messages = messageRepository.findAll();
        assertThat(messages.size()).isZero();
    }

    /**
     * Testing if DELETE /api/v1/messages/{messageId} returns not found when ID doesn't exist
     */
    @Test
    void shouldReturnNotFoundIfIdDoesntExistInDeleteMessageById() throws Exception {
        mockMvc.perform(delete("/api/v1/messages/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Testing if DELETE /api/v1/messages/{messageId} returns bad request if we send invalid ID
     */
    @Test
    void shouldReturnBadRequestIfDeleteMessageIdIsNotValid() throws Exception {
        mockMvc.perform(delete("/api/v1/messages/as"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Testing if PUT /api/v1/messages/{messageId} returns 200 and updates the message in database
     */
    @Test
    void shouldUpdateMessageWithValidIdAndValidMessageRequest() throws Exception {
        //given
        Message message = new Message(1L, "Title", "Text Text with 20 letters");
        MessageRequest messageRequest = new MessageRequest("Update Title", "Updated Text Text with 20 letters");
        messageRepository.save(message);
        String messageId = messageRepository.findAll().get(0).getMessageId().toString();
        String requestJson = objectMapper.writeValueAsString(messageRequest);
        //when
        MvcResult result = mockMvc.perform(put("/api/v1/messages/" + messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Message updatedMessageFromResponse = objectMapper.readValue(responseBody, Message.class);
        assertThat(updatedMessageFromResponse.getText()).isEqualTo(messageRequest.getText());
    }

    /**
     * Testing if PUT /api/v1/messages/{messageId} returns not found if message with ID doesn't exist
     */
    @Test
    void shouldReturnNotFoundIfIdDoesntExistInUpdateMessage() throws Exception {
        //given
        Message message = new Message(1L, "Title", "Text Text with 20 letters");
        MessageRequest messageRequest = new MessageRequest("Update Title", "Updated Text Text with 20 letters");
        messageRepository.save(message);
        String requestJson = objectMapper.writeValueAsString(messageRequest);
        //when
        mockMvc.perform(put("/api/v1/messages/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("utf-8"))
                .andExpect(status().isNotFound());
    }

    /**
     * Testing if PUT /api/v1/messages/{messageId} returns bad request if message ID is not valid
     */
    @Test
    void shouldReturnBadRequestIfUpdateMessageIdIsNotValid() throws Exception {
        //given
        Message message = new Message(1L, "Title", "Text Text with 20 letters");
        MessageRequest messageRequest = new MessageRequest("Update Title", "Updated Text Text with 20 letters");
        messageRepository.save(message);
        String requestJson = objectMapper.writeValueAsString(messageRequest);
        //when
        mockMvc.perform(put("/api/v1/messages/as")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Testing if PUT /api/v1/messages/{messageId} returns bad request if updateMessage body is not valid
     */
    @Test
    void shouldReturnBadRequestIfUpdateBodyIsNotValid() throws Exception {
        //given
        Message message = new Message(1L, "Title", "Text Text with 20 letters");
        MessageRequest messageRequest = new MessageRequest("Update Title", "Bad Text");
        messageRepository.save(message);
        String requestJson = objectMapper.writeValueAsString(messageRequest);
        //when
        mockMvc.perform(put("/api/v1/messages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }
}