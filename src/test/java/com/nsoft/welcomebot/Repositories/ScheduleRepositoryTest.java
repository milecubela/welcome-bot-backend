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
    void shouldFindAllActiveSchedules() {
        //given
        ScheduleRequest scheduleRequest1 = new ScheduleRequest(true, true, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Schedule schedule1 = new Schedule(scheduleRequest1);
        ScheduleRequest scheduleRequest2 = new ScheduleRequest(true, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Schedule schedule2 = new Schedule(scheduleRequest2);
        _scheduleRepositoryTest.save(schedule1);
        _scheduleRepositoryTest.save(schedule2);

        //when
        List<Schedule> result = _scheduleRepositoryTest.findAllActiveSchedules();
        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void shouldtFindAnyActiveSchedules() {
        //given
        ScheduleRequest scheduleRequest1 = new ScheduleRequest(true, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Schedule schedule1 = new Schedule(scheduleRequest1);
        ScheduleRequest scheduleRequest2 = new ScheduleRequest(true, false, LocalDateTime.now(), SchedulerInterval.MINUTE, "testingchannel", 12L);
        Schedule schedule2 = new Schedule(scheduleRequest2);
        _scheduleRepositoryTest.save(schedule1);
        _scheduleRepositoryTest.save(schedule2);

        //when
        List<Schedule> result = _scheduleRepositoryTest.findAllActiveSchedules();
        //then
        assertThat(result.isEmpty()).isTrue();
    }
}