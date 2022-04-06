package com.nsoft.welcomebot.Services;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.NotFoundException;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository _mockScheduleRepository;
    @Mock
    private MessageRepository _mockMessageRepository;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleService = new ScheduleService(_mockScheduleRepository, _mockMessageRepository);
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
    void shouldThrowOnDeleteIfDeleteIdDoesntExist() {
        Long id = anyLong();
        assertThatThrownBy(() -> scheduleService.deleteSchedule(id)).isInstanceOf(NotFoundException.class).hasMessageContaining(("Schedule with ID " + id + " not found"));
    }

    @Test
    void shouldThrowOnUpdateIfUpdateIdDoesntExist() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Long id = anyLong();
        assertThatThrownBy(() -> scheduleService.updateSchedule(id, scheduleRequest)).isInstanceOf(NotFoundException.class).hasMessageContaining(("Schedule with ID " + id + " not found"));
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
    void shouldThrowOnGetByIdIfIdDoesntExist() {
        //given
        Long scheduleId = 1L;
        given(_mockScheduleRepository.findById(scheduleId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> scheduleService.getScheduleById(scheduleId)).isInstanceOf(NotFoundException.class);
    }
}
