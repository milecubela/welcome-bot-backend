package com.nsoft.welcomebot.Scheduler;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import com.slack.api.bolt.App;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
@ExtendWith(MockitoExtension.class)
class PeriodicalMessagesTest {
    @MockBean
    private App app;

    @MockBean
    private Credentials credentials;

    @Mock
    private PeriodicalMessages periodicalMessages;

    @Autowired
    private ScheduleRepository scheduleRepositoryH2;

    @Test
    void canSetActiveToFalseAfterExecutionIfRepeatIsFalse() {
        periodicalMessages = new PeriodicalMessages(scheduleRepositoryH2, app, credentials);
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(5), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setScheduleId(1L);
        //when
        periodicalMessages.deactivateSchedule(schedule);
        Optional<Schedule> result = scheduleRepositoryH2.findById(1L);
        Schedule returnSchedule = result.get();
        //then
        assertThat(returnSchedule.isActive()).isFalse();
    }

    /**
     * Test makes sure the next run date is set a minute from now.
     * To break this test compare the next run date to a timestamp greater than 1 minute from now.
     * RunDate is set to .now()-1s because of a millisecond difference in execution time which would show the minute difference as 0 instead of 1
     * without it the difference between the two dates would be ~59.997s and the ChronoUnit comparison would show 0 minutes difference.
     */
    @Test
    void canAddOneMinuteToNextRunDate() {
        periodicalMessages = new PeriodicalMessages(scheduleRepositoryH2, app, credentials);

        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now().minusSeconds(1), SchedulerInterval.MINUTE, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setScheduleId(1L);

        //when
        periodicalMessages.setNextRunDate(schedule);
        Optional<Schedule> result = scheduleRepositoryH2.findById(1L);
        Schedule returnSchedule = result.get();
        //then

        assertThat(ChronoUnit.MINUTES.between(schedule.getRunDate(), returnSchedule.getNextRun())).isEqualTo(5);
    }

    @Test
    void canAddOneHourToNextRunDate() {
        periodicalMessages = new PeriodicalMessages(scheduleRepositoryH2, app, credentials);

        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now(), SchedulerInterval.HOUR, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setScheduleId(1L);

        //when
        periodicalMessages.setNextRunDate(schedule);
        Optional<Schedule> result = scheduleRepositoryH2.findById(1L);
        Schedule returnSchedule = result.get();
        //then
        assertThat(ChronoUnit.MINUTES.between(schedule.getRunDate(), returnSchedule.getNextRun())).isEqualTo(60);
    }

    @Test
    void canAddOneDayToNextRunDate() {
        periodicalMessages = new PeriodicalMessages(scheduleRepositoryH2, app, credentials);

        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, true, LocalDateTime.now(), SchedulerInterval.DAY, "testchannel", 1L);
        Schedule schedule = new Schedule(scheduleRequest);
        schedule.setScheduleId(1L);

        //when
        periodicalMessages.setNextRunDate(schedule);
        Optional<Schedule> result = scheduleRepositoryH2.findById(1L);
        Schedule returnSchedule = result.get();
        //then
        assertThat(ChronoUnit.HOURS.between(schedule.getRunDate(), returnSchedule.getNextRun())).isEqualTo(24);
    }
}

