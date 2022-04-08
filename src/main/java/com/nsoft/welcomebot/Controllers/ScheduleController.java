package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
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
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Object> getSchedules(@Valid @RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pagesize", required = false) Integer pagesize) {
        if (offset == null || pagesize == null) {
            List<Schedule> scheduleList = scheduleService.getSchedules();
            return new ResponseEntity<>(scheduleList, HttpStatus.OK);
        }
        Page<Schedule> pageSchedules = scheduleService.findAllPaginated(offset, pagesize);
        return new ResponseEntity<>(pageSchedules, HttpStatus.OK);
    }

    @GetMapping(path = "{scheduleID}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long scheduleID) {
        Schedule schedule = scheduleService.getScheduleById(scheduleID);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        Schedule returnSchedule = scheduleService.createNewSchedule(scheduleRequest);
        return new ResponseEntity<>(returnSchedule, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity<>("Schedule deleted", HttpStatus.OK);
    }

    @PutMapping(path = "/{scheduleId}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable("scheduleId") Long scheduleId, @Valid @RequestBody ScheduleRequest scheduleRequest) {
        Schedule updatedSchedule = scheduleService.updateSchedule(scheduleId, scheduleRequest);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @PostMapping(path = "/toggle")
    public ResponseEntity<String> toggleGif() {
        scheduleService.toggleGifs();
        return new ResponseEntity<>("Successfully toggled gif sending", HttpStatus.OK);
    }
}
