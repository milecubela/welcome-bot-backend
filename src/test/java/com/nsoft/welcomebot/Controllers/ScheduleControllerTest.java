package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Services.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest
class ScheduleControllerTest {

    private ScheduleController scheduleControllerTest;
    @MockBean
    private ScheduleService scheduleServiceTest;

    @BeforeEach
    void setUp() {
        scheduleControllerTest = new ScheduleController(scheduleServiceTest);
    }

    @Test
    void shouldVerifyGetSchedulesMethodIsCalled() {
        //when
        scheduleControllerTest.getSchedules(null, null);

        //then
        verify(scheduleServiceTest).getSchedules();
    }

    @Test
    void shouldVerifyGetPaginatedSchedulesMethodIsCalled() {
        //when
        scheduleControllerTest.getSchedules(0, 15);

        //then
        verify(scheduleServiceTest).findAllPaginated(0, 15);
    }
}