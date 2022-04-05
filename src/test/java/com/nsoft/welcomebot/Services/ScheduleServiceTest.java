package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Autowired
    private ScheduleRepository _scheduleRepositoryTest;
    @Autowired
    private MessageRepository _messageRepositoryTest;
    @Mock
    private ScheduleRepository _mockScheduleRepository;
    @Mock
    private MessageRepository _mockMessageRepository;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleService = new ScheduleService(_mockScheduleRepository, _mockMessageRepository);
    }

    @AfterEach
    void tearDown() {
        _scheduleRepositoryTest.deleteAll();
    }

    @Test
    void shouldGetSchedules() {
        _mockScheduleRepository.findAll();
        verify(_mockScheduleRepository).findAll();
    }

    @Test
    void shouldThrowOnCreateScheduleIfMessageDoesntExist() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        assertThatThrownBy(() -> scheduleService.createNewSchedule(scheduleRequest)).isInstanceOf(NotFoundException.class).hasMessageContaining(("Message with ID " + scheduleRequest.getMessageId() + " not found!"));
    }

    @Test
    void shouldCreateNewIfMessageExists() {
        scheduleService = new ScheduleService(_scheduleRepositoryTest, _messageRepositoryTest);
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 1L);
        MessageRequest messageRequest = new MessageRequest("title of text message", "text of message should be at least 20 letters long");
        Message message = new Message(messageRequest);
        _messageRepositoryTest.save(message);
        scheduleService.createNewSchedule(scheduleRequest);
        var expected = _messageRepositoryTest.findAll().isEmpty();
        assertThat(expected).isFalse();
    }

    @Test
    void shouldThrowOnDeleteIfDeleteIdDoesntExist() {
        Long id = anyLong();
        assertThatThrownBy(() -> scheduleService.deleteSchedule(id)).isInstanceOf(NotFoundException.class).hasMessageContaining(("Schedule with ID " + id + " not found"));
    }

    @Test
    void shouldDeleteSchedule() {
        //given
        scheduleService = new ScheduleService(_scheduleRepositoryTest, _messageRepositoryTest);
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        _scheduleRepositoryTest.save(schedule);
        Schedule returnSchedule = _scheduleRepositoryTest.findAll().get(0);

        //when
        scheduleService.deleteSchedule(returnSchedule.getScheduleId());

        //then
        assertThat(_scheduleRepositoryTest.findAll().isEmpty()).isTrue();
    }

    @Test
    void shouldThrowOnUpdateIfUpdateIdDoesntExist() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Long id = anyLong();
        assertThatThrownBy(() -> scheduleService.updateSchedule(id, scheduleRequest)).isInstanceOf(NotFoundException.class).hasMessageContaining(("Schedule with ID " + id + " not found"));
    }

    @Test
    void shouldUpdateSchedule() {
        // given
        Message message = new Message(new MessageRequest("some title", "text za testiranje testa tost."));
        message.setMessageId(1L);
        Long scheduleId = 1L;
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "textchanle", 1L);
        ScheduleRequest scheduleRequestUpdate = new ScheduleRequest(true, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "textchanle", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setScheduleId(scheduleId);
        given(_mockScheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
        given(_mockMessageRepository.findById(message.getMessageId())).willReturn(Optional.of(message));

        // when
        Schedule returnSchedule = scheduleService.updateSchedule(scheduleId, scheduleRequestUpdate);

        // then
        assertThat(returnSchedule.isActive()).isFalse();
    }

    @Test
    void shouldThrowOnUpdateIfMessageDoesntExist() {
        //given
        Long messageId = 1L;
        Long scheduleId = 1L;
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        given(_mockMessageRepository.findById(messageId)).willReturn(Optional.empty());
        given(_mockScheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));

        //when
        //then
        assertThatThrownBy(() -> scheduleService.updateSchedule(scheduleId, scheduleRequest)).isInstanceOf(NotFoundException.class).hasMessageContaining(("Message with ID " + messageId + " not found"));
    }

    @Test
    void shouldThrowOnUpdateIfScheduleDoesntExist() {
        //given
        Long scheduleId = 1L;
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", 1L);
        lenient();
        given(_mockScheduleRepository.findById(scheduleId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> scheduleService.updateSchedule(scheduleId, scheduleRequest)).isInstanceOf(NotFoundException.class).hasMessageContaining(("Schedule with ID " + scheduleId + " not found"));
    }

    @Test
    void shouldGetScheduleById() {
        //given
        Long scheduleId = 1L;
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setScheduleId(scheduleId);
        given(_mockScheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));

        //when
        Schedule returnSchedule = scheduleService.getScheduleById(scheduleId);

        //then
        assertThat(returnSchedule.getScheduleId()).isEqualTo(schedule.getScheduleId());
    }

    @Test
    void shouldThrowOnGetByIdIfIdDoesntExist() {
        //given
        Long scheduleId = 1L;
        given(_mockScheduleRepository.findById(scheduleId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> scheduleService.getScheduleById(scheduleId)).isInstanceOf(NotFoundException.class);
    }
}
