package com.nsoft.welcomebot.Controllers;
import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @GetMapping(path = "{scheduleID}")
    public Schedule getScheduleById(@PathVariable Long scheduleID){return _scheduleSerivce.getScheduleById(scheduleID);}

    @PostMapping
    public void createSchedule(@Valid @RequestBody Schedule schedule){
        _scheduleSerivce.createNewSchedule(schedule);
    }

    @DeleteMapping(path = "{scheduleId}")
    public void deleteSchedule(@PathVariable Long scheduleId){_scheduleSerivce.deleteSchedule(scheduleId);}

    @PutMapping
    public void updateSchedule(@Valid @RequestBody Schedule schedule){ _scheduleSerivce.updateSchedule(schedule);}

    @GetMapping("/paginated/{offset}/{pagesize}")
    public Page<Schedule> getPaginatedMessages(@PathVariable int offset, @PathVariable int pagesize){
        Page<Schedule> schedules=_scheduleSerivce.findAllPaginated(offset, pagesize);
        return schedules;
    }

}
