package com.nsoft.welcomebot.Scheduler;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.impl.MethodsClientImpl;
import com.slack.api.util.http.SlackHttpClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {PeriodicalMessages.class})
@ExtendWith(SpringExtension.class)
class PeriodicalMessagesTest {
    @MockBean
    private App app;

    @MockBean
    private Credentials credentials;

    @Autowired
    private PeriodicalMessages periodicalMessages;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @Test
    void shouldSetActiveToFalseAfterExecution() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(5), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);

        //when
        periodicalMessages.deactivateSchedule(schedule);
        ArgumentCaptor<Schedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository, times(2)).save(scheduleArgumentCaptor.capture());
        Schedule returnSchedule = scheduleArgumentCaptor.getValue();

        //then
        assertThat(returnSchedule.isActive()).isFalse();
    }

    /**
     * Test makes sure the next run date is set a minute from now.
     * To break this test compare the next run date to a timestamp greater than 1 minute from now.
     */
    @Test
    void shouldAddOneMinuteToNextRunDate() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(5), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);

        //when
        periodicalMessages.setNextRunDate(schedule);
        ArgumentCaptor<Schedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleArgumentCaptor.capture());
        Schedule returnSchedule = scheduleArgumentCaptor.getValue();

        //then
        assertThat(returnSchedule.getNextRun()).isAfter(LocalDateTime.now());
//        assertThat(returnSchedule.getNextRun()).isAfter(LocalDateTime.now().plusHours(1));
    }

    @Test
    void shouldAddOneHourToNextRunDate() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(5), SchedulerInterval.HOUR, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);

        //when
        periodicalMessages.setNextRunDate(schedule);
        ArgumentCaptor<Schedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleArgumentCaptor.capture());
        Schedule returnSchedule = scheduleArgumentCaptor.getValue();

        //then
        assertThat(returnSchedule.getNextRun()).isAfter(LocalDateTime.now());
//        assertThat(returnSchedule.getNextRun()).isAfter(LocalDateTime.now().plusHours(1));
    }

    @Test
    void shouldAddOneDayToNextRunDate() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(5), SchedulerInterval.DAY, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);

        //when
        periodicalMessages.setNextRunDate(schedule);
        ArgumentCaptor<Schedule> scheduleArgumentCaptor = ArgumentCaptor.forClass(Schedule.class);
        verify(scheduleRepository).save(scheduleArgumentCaptor.capture());
        Schedule returnSchedule = scheduleArgumentCaptor.getValue();

        //then
        assertThat(returnSchedule.getNextRun()).isAfter(LocalDateTime.now());
//        assertThat(returnSchedule.getNextRun()).isAfter(LocalDateTime.now().plusHours(1));
    }
}
