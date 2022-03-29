package com.nsoft.welcomebot.Entities;

import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ScheduleTest {

    @Test
    @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void canSetNextRunToRunDate() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Schedule schedule = new Schedule(scheduleRequest);
        boolean expected = schedule.getNextRun().isEqual(scheduleRequest.getRunDate());
        assertThat(expected).isTrue();
    }
}