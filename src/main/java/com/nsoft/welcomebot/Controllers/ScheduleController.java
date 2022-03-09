package com.nsoft.welcomebot.Controllers;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/schedules")
public class ScheduleController {
    private final ScheduleService _scheduleSerivce;

    @Autowired
    public ScheduleController(ScheduleService scheduleSerivce) {
        _scheduleSerivce = scheduleSerivce;

    }

    @GetMapping
    public List<Schedule> getSchedules(){
        return _scheduleSerivce.getSchedules();
    }

    @PostMapping
    public void createSchedule(@Valid @RequestBody Schedule schedule){
        _scheduleSerivce.createNewSchedule(schedule);
    }
}
