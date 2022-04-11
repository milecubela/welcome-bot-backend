package com.nsoft.welcomebot.IntegrationTests.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Services.ScheduleService;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ScheduleControllerIT {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MessageRepository messageRepository;

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void shouldReturnAllSchedules() throws Exception {
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Message msg = messageRepository.findAll().get(0);
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", msg.getMessageId());
        Schedule schedule1 = new Schedule(scheduleRequest);
        Schedule schedule2 = new Schedule(scheduleRequest);
        schedule1.setMessage(msg);
        schedule2.setMessage(msg);
        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        MvcResult result = mockMvc.perform(get("/api/v1/schedules")).andDo(print()).andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        List<Schedule> scheduleList = objectMapper.readValue(responseBody, new TypeReference<>() {
        });
        assertThat(scheduleList.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnPaginatedSchedules() throws Exception {
        Message message = new Message("texttexttext", "text text with 20 letters");
        messageRepository.save(message);
        Schedule schedule1 = new Schedule(1L, false, false, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), SchedulerInterval.MINUTE, message, "page1channel");
        Schedule schedule2 = new Schedule(2L, false, false, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), SchedulerInterval.MINUTE, message, "page1channel");
        Schedule schedule3 = new Schedule(3L, true, true, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), SchedulerInterval.MINUTE, message, "page2channel");
        Schedule schedule4 = new Schedule(4L, true, true, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), SchedulerInterval.MINUTE, message, "page2channel");
        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);
        scheduleRepository.save(schedule4);
        MvcResult result = mockMvc.perform(get("/api/v1/schedules?offset=1&pagesize=2")).andDo(print()).andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        MvcResult result2 = mockMvc.perform(get("/api/v1/schedules?offset=0&pagesize=2")).andDo(print()).andExpect(status().isOk()).andReturn();
        String responseBody2 = result2.getResponse().getContentAsString();
        assertThat(responseBody2).contains("page1channel");
        assertThat(responseBody).contains("page2channel");
    }

    @Test
    void shouldGetScheduleById() throws Exception {
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Schedule schedule1 = new Schedule(2L, false, false, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), SchedulerInterval.MINUTE, message, "page1channel");
        scheduleRepository.save(schedule1);
        Schedule returnSchedule = scheduleRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(get("/api/v1/schedules/" + returnSchedule.getScheduleId())).andDo(print()).andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Schedule schedule = objectMapper.readValue(responseBody, Schedule.class);
        assertThat(schedule.getScheduleId()).isEqualTo(returnSchedule.getScheduleId());
    }

    @Test
    void shouldThrowNotFoundIfScheduleDoesntExist() throws Exception {
        mockMvc.perform(get("/api/v1/schedules/11")).andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteScheduleById() throws Exception {
        scheduleService = new ScheduleService(scheduleRepository, messageRepository);
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Message msg = messageRepository.findAll().get(0);
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule1 = new Schedule(scheduleRequest);
        schedule1.setMessage(msg);
        scheduleRepository.save(schedule1);
        Schedule returnSchedule = scheduleRepository.findAll().get(0);
        mockMvc.perform(delete("/api/v1/schedules/" + returnSchedule.getScheduleId())).andExpect(status().isOk());
        List<Schedule> schedules = scheduleRepository.findAll();
        assertThat(schedules.isEmpty()).isTrue();
    }

    @Test
    void shouldThrowOnDeleteIfScheduleIdDoesntExist() throws Exception {
        mockMvc.perform(delete("/api/v1/schedules/11")).andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowOnCreateIfMessageIdDoesntExist() throws Exception {
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "page1channel", 12L);
        String requestJson = objectMapper.writeValueAsString(scheduleRequest);
        //when
        mockMvc.perform(post("/api/v1/schedules").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8")).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewSchedule() throws Exception {
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Message returnMessage = messageRepository.findAll().get(0);
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "page1channel", returnMessage.getMessageId());
        String requestJson = objectMapper.writeValueAsString(scheduleRequest);
        //when
        mockMvc.perform(post("/api/v1/schedules").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8")).andExpect(status().isCreated());
        List<Schedule> result = scheduleRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void shouldThrowOnScheduleUpdateIfIdIsInvalid() throws Exception {
        ScheduleRequest badRequest = new ScheduleRequest(false, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "TEST", 12L);
        var requestJson = objectMapper.writeValueAsString(badRequest);
        mockMvc.perform(put("/api/v1/schedules/5").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8")).andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowOnScheduleUpdateIfMessageIdIsInvalid() throws Exception {
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Schedule schedule1 = new Schedule(1L, false, false, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), SchedulerInterval.MINUTE, message, "testchannel");
        scheduleRepository.save(schedule1);
        ScheduleRequest badRequest = new ScheduleRequest(false, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "TEST", 12L);
        var requestJson = objectMapper.writeValueAsString(badRequest);
        mockMvc.perform(put("/api/v1/schedules/1").contentType(MediaType.APPLICATION_JSON).content(requestJson).
                characterEncoding("utf-8")).andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateSchedule() throws Exception {
        Message message = new Message("Title", "Text Text with 20 letters");
        messageRepository.save(message);
        Schedule schedule1 = new Schedule(2L, false, false, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), SchedulerInterval.MINUTE, message, "testchannel");
        scheduleRepository.save(schedule1);
        Message returnMessage = messageRepository.findAll().get(0);
        Schedule returnedSchedule = scheduleRepository.findAll().get(0);
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "TEST", returnMessage.getMessageId());
        var requestJson = objectMapper.writeValueAsString(scheduleRequest);
        MvcResult result = mockMvc.perform(put("/api/v1/schedules/" + returnedSchedule.getScheduleId()).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).characterEncoding("utf-8")).andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Schedule newSchedule = objectMapper.readValue(responseBody, Schedule.class);
        assertThat(newSchedule.isActive()).isTrue();
    }

}
