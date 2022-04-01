package com.nsoft.welcomebot.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.TriggerRepository;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
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


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TriggerControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TriggerRepository triggerRepository;
    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void cleanRepository() {
        triggerRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void canCreateTrigger() throws Exception {
        //given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        message = messageRepository.findAll().get(0);
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                message.getMessageId(),
                TriggerEvent.APP_MENTION_EVENT
        );
        String requestJson = objectMapper.writeValueAsString(triggerRequest);
        //then
        mockMvc.perform(post("/api/v1/triggers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void canThrowWhenCreateTrigger() throws Exception {
        //given
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                1L,
                TriggerEvent.APP_MENTION_EVENT
        );
        String requestJson = objectMapper.writeValueAsString(triggerRequest);
        //then
        mockMvc.perform(post("/api/v1/triggers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void canGetAllTriggers() throws Exception {
        //given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        message = messageRepository.findAll().get(0);
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                message.getMessageId(),
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        Trigger trigger2 = new Trigger(triggerRequest);
        trigger.setMessage(message);
        trigger2.setMessage(message);
        triggerRepository.save(trigger);
        triggerRepository.save(trigger2);
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/triggers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseBody = result.getResponse().getContentAsString();
        List<Trigger> triggerList = objectMapper.readValue(responseBody, new TypeReference<List<Trigger>>() {
        });
        assertThat(triggerList.size()).isEqualTo(2);
    }

    @Test
    void canReturnEmptyArrayWhenGetAllTriggers() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/triggers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseBody = result.getResponse().getContentAsString();
        List<Trigger> triggerList = objectMapper.readValue(responseBody, new TypeReference<List<Trigger>>() {
        });
        assertThat(triggerList.size()).isEqualTo(0);
    }

    @Test
    void canGetPaginatedTriggers() throws Exception {
        //given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        message = messageRepository.findAll().get(0);
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                message.getMessageId(),
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        Trigger trigger2 = new Trigger(triggerRequest);
        trigger.setMessage(message);
        trigger2.setMessage(message);
        triggerRepository.save(trigger);
        triggerRepository.save(trigger2);
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/triggers?offset=1&pagesize=5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("offset");
    }

    @Test
    void canGetTriggerById() throws Exception {
        //given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        message = messageRepository.findAll().get(0);
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                message.getMessageId(),
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        trigger.setMessage(message);
        triggerRepository.save(trigger);
        String triggerId = triggerRepository.findAll().get(0).getTriggerId().toString();
        //then
        mockMvc.perform(get("/api/v1/triggers/" + triggerId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void canThrowGetTriggerById() throws Exception {
        mockMvc.perform(get("/api/v1/triggers/" + 345L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void canDeleteTrigger() throws Exception {
        //given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        message = messageRepository.findAll().get(0);
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                message.getMessageId(),
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        trigger.setMessage(message);
        triggerRepository.save(trigger);
        String triggerId = triggerRepository.findAll().get(0).getTriggerId().toString();
        //then
        mockMvc.perform(delete("/api/v1/triggers/" + triggerId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void canThrowWhenDeleteTrigger() throws Exception {
        mockMvc.perform(delete("/api/v1/triggers/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void canUpdateTrigger() throws Exception {
        //given
        Message message = new Message(
                new MessageRequest(
                        "Neki title",
                        "Ovo je neki tekst za testiranje valjda."
                )
        );
        messageRepository.save(message);
        message = messageRepository.findAll().get(0);
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                message.getMessageId(),
                TriggerEvent.APP_MENTION_EVENT
        );
        Trigger trigger = new Trigger(triggerRequest);
        trigger.setMessage(message);
        triggerRepository.save(trigger);
        String triggerId = triggerRepository.findAll().get(0).getTriggerId().toString();
        String requestJson = objectMapper.writeValueAsString(triggerRequest);
        //then
        mockMvc.perform(put("/api/v1/triggers/" + triggerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void canThrowOnUpdateTrigger() throws Exception {
        //given
        TriggerRequest triggerRequest = new TriggerRequest(
                "channel",
                true,
                1L,
                TriggerEvent.APP_MENTION_EVENT
        );
        String requestJson = objectMapper.writeValueAsString(triggerRequest);
        //then
        mockMvc.perform(get("/api/v1/triggers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
