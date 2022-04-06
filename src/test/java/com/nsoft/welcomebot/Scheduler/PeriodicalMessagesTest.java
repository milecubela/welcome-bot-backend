package com.nsoft.welcomebot.Scheduler;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Services.SlackService;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
@ExtendWith(MockitoExtension.class)
class PeriodicalMessagesTest {
    @MockBean
    private SlackService slackService;

    @MockBean
    private Credentials credentials;

    @MockBean
    private PeriodicalMessages periodicalMessages;

    @Autowired
    private ScheduleRepository scheduleRepositoryH2;

    @BeforeEach
    void setUp() {
        periodicalMessages = new PeriodicalMessages(scheduleRepositoryH2, credentials, slackService);
    }

    @AfterEach
    void tearDown() {
        scheduleRepositoryH2.deleteAll();
    }


    @Test
    void shouldSetActiveToFalseAfterExecutionIfRepeatIsFalse() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(5), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        scheduleRepositoryH2.save(schedule);
        Long scheduleId = scheduleRepositoryH2.findAll().get(0).getScheduleId();
        //when
        periodicalMessages.deactivateSchedule(schedule);
        Optional<Schedule> result = scheduleRepositoryH2.findById(scheduleId);
        Schedule returnSchedule = result.get();
        //then
        assertThat(returnSchedule.isActive()).isFalse();
    }

    /**
     * Test makes sure the next run date is set a minute from now.
     * To break this test compare the next run date to a timestamp greater than 5 minutes from now.
     */
    @Test
    void shouldAddFiveMinutesToNextRunDate() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(1), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        scheduleRepositoryH2.save(schedule);
        Long scheduleId = scheduleRepositoryH2.findAll().get(0).getScheduleId();
        //when
        periodicalMessages.setNextRunDate(schedule);
        Schedule result = scheduleRepositoryH2.getById(scheduleId);
        //then

        assertThat(ChronoUnit.MINUTES.between(schedule.getRunDate(), result.getNextRun())).isEqualTo(5);
    }

    @Test
    void canAddOneHourToNextRunDate() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now(), SchedulerInterval.HOUR, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        scheduleRepositoryH2.save(schedule);
        Long scheduleId = scheduleRepositoryH2.findAll().get(0).getScheduleId();
        //when
        periodicalMessages.setNextRunDate(schedule);
        Optional<Schedule> result = scheduleRepositoryH2.findById(scheduleId);
        Schedule returnSchedule = result.get();
        //then
        assertThat(ChronoUnit.MINUTES.between(schedule.getRunDate(), returnSchedule.getNextRun())).isEqualTo(60);
    }

    @Test
    void shouldAddOneDayToNextRunDate() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now(), SchedulerInterval.DAY, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        scheduleRepositoryH2.save(schedule);
        Long scheduleId = scheduleRepositoryH2.findAll().get(0).getScheduleId();

        //when
        periodicalMessages.setNextRunDate(schedule);
        Optional<Schedule> result = scheduleRepositoryH2.findById(scheduleId);
        Schedule returnSchedule = result.get();
        //then
        assertThat(ChronoUnit.HOURS.between(schedule.getRunDate(), returnSchedule.getNextRun())).isEqualTo(24);
    }
}

