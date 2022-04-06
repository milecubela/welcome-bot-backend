package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
@ExtendWith(MockitoExtension.class)
class ScheduleServiceIntegrationTest {

    @Autowired
    private ScheduleRepository _scheduleRepositoryTest;
    @Autowired
    private MessageRepository _messageRepositoryTest;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        _scheduleRepositoryTest.deleteAll();
        _messageRepositoryTest.deleteAll();
        scheduleService = new ScheduleService(_scheduleRepositoryTest, _messageRepositoryTest);
    }

    @AfterEach
    void tearDown() {
        _scheduleRepositoryTest.deleteAll();
    }

    @Test
    void shouldCreateNewIfMessageExists() {
        MessageRequest messageRequest = new MessageRequest("title of text message", "text of message should be at least 20 letters long");
        Message message = new Message(messageRequest);
        _messageRepositoryTest.save(message);
        Long msgId = _messageRepositoryTest.findAll().get(0).getMessageId();
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", msgId);
        scheduleService.createNewSchedule(scheduleRequest);
        var expected = _scheduleRepositoryTest.findAll().isEmpty();
        assertThat(expected).isFalse();
    }

    @Test
    void shouldDeleteSchedule() {
        //given
        Message message = new Message(new MessageRequest("some title", "text za testiranje testa tost."));
        _messageRepositoryTest.save(message);
        Long messageId = _messageRepositoryTest.findAll().get(0).getMessageId();
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", messageId);
        Schedule schedule = new Schedule(scheduleRequest);
        _scheduleRepositoryTest.save(schedule);
        Schedule returnSchedule = _scheduleRepositoryTest.findAll().get(0);

        //when
        scheduleService.deleteSchedule(returnSchedule.getScheduleId());

        //then
        assertThat(_scheduleRepositoryTest.findAll().isEmpty()).isTrue();
    }

    @Test
    void shouldUpdateSchedule() {
        // given
        Message message = new Message(new MessageRequest("some title", "text za testiranje testa tost."));
        _messageRepositoryTest.save(message);
        Long messageId = _messageRepositoryTest.findAll().get(0).getMessageId();
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "textchanle", messageId);
        ScheduleRequest scheduleRequestUpdate = new ScheduleRequest(true, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "textchanle", messageId);
        Schedule schedule = new Schedule(scheduleRequest);
        _scheduleRepositoryTest.save(schedule);
        Long scheduleId = _scheduleRepositoryTest.findAll().get(0).getScheduleId();

        // when
        Schedule returnSchedule = scheduleService.updateSchedule(scheduleId, scheduleRequestUpdate);

        // then
        assertThat(returnSchedule.isActive()).isFalse();
    }

    @Test
    void shouldGetScheduleById() {
        //given
        Message message = new Message(new MessageRequest("some title", "text za testiranje testa tost."));
        _messageRepositoryTest.save(message);
        Long messageId = _messageRepositoryTest.findAll().get(0).getMessageId();
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", messageId);
        Schedule schedule = new Schedule(scheduleRequest);
        _scheduleRepositoryTest.save(schedule);
        Long scheduleId = _scheduleRepositoryTest.findAll().get(0).getScheduleId();
        //when
        Schedule returnSchedule = scheduleService.getScheduleById(scheduleId);

        //then
        assertThat(returnSchedule.getScheduleId()).isEqualTo(scheduleId);
    }
}
