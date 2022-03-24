package com.nsoft.welcomebot.Repositories;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository _scheduleRepositoryTest;

    @AfterEach
    void tearDown() {
        _scheduleRepositoryTest.deleteAll();
    }

    @Test
    void itShouldFindAllActiveSchedules() {
        //given
        ScheduleRequest scheduleRequest = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Schedule schedule = new Schedule(scheduleRequest);
        _scheduleRepositoryTest.save(schedule);

        //when
        List<Schedule> expectedtohaveone = _scheduleRepositoryTest.findAllActiveSchedules();
        boolean expected = expectedtohaveone.isEmpty();
        //then
        assertThat(expected).isFalse();
    }

    @Test
    void itShouldntFindAnyActiveSchedules() {
        //given
        ScheduleRequest scheduleRequest = new ScheduleRequest(false, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Schedule schedule = new Schedule(scheduleRequest);
        _scheduleRepositoryTest.save(schedule);

        //when
        List<Schedule> expectedtobeempty = _scheduleRepositoryTest.findAllActiveSchedules();
        boolean expected = expectedtobeempty.isEmpty();
        //then
        assertThat(expected).isTrue();
    }
}