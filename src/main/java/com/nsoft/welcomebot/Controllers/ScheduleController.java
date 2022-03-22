package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Services.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/schedules")
public class ScheduleController {
    private final ScheduleService _scheduleSerivce;

    @GetMapping
    public ResponseEntity<Object> getSchedules(@Valid @RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pagesize", required = false) Integer pagesize) {
        if (offset == null || pagesize == null) {
            List<Schedule> scheduleList = _scheduleSerivce.getSchedules();
            return new ResponseEntity<>(scheduleList, HttpStatus.OK);
        }
        Page<Schedule> pageSchedules = _scheduleSerivce.findAllPaginated(offset, pagesize);
        return new ResponseEntity<>(pageSchedules, HttpStatus.OK);
    }

    @GetMapping(path = "{scheduleID}")
    public Schedule getScheduleById(@PathVariable Long scheduleID) {
        return _scheduleSerivce.getScheduleById(scheduleID);
    }

    @PostMapping
    public void createSchedule(@Valid @RequestBody Schedule schedule) {
        _scheduleSerivce.createNewSchedule(schedule);
    }

    @DeleteMapping(path = "{scheduleId}")
    public void deleteSchedule(@PathVariable Long scheduleId) {
        _scheduleSerivce.deleteSchedule(scheduleId);
    }

    @PutMapping
    public void updateSchedule(@Valid @RequestBody Schedule schedule) {
        _scheduleSerivce.updateSchedule(schedule);
    }
}
