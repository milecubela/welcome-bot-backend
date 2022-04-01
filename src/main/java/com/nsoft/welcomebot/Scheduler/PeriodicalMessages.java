package com.nsoft.welcomebot.Scheduler;


import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Services.SlackService;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import com.slack.api.methods.SlackApiException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PeriodicalMessages {
    private final ScheduleRepository _scheduleRepository;
    private final SlackService slackService;

    @Scheduled(fixedDelay = 30000)
    public void sendScheduledMessages() throws SlackApiException, IOException {
        List<Schedule> schedules = _scheduleRepository.findAllActiveSchedules();
        // this list is composed of all active repeating schedules
        List<Schedule> startRunningList = new ArrayList<>();

        // setDefaultValues sets next run to run date value  (default) +  sendAtScheduledRunDate checks if the run date is now
        for (Schedule schedule : schedules) {
            sendAtScheduledRunDate(schedule);
            //   separates all repeating schedules
            if (checkIsRepeating(schedule)) startRunningList.add(schedule);
        }

        //   repeating
        for (Schedule schedule : startRunningList) {
            if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
                sendMessage(schedule);
            }
        }
    }

    public boolean checkIsRepeating(Schedule schedule) {
        return schedule.isActive() && schedule.isRepeat();
    }

    public void sendMessage(Schedule schedule) throws SlackApiException, IOException {
        slackService.postMessage(schedule.getChannel(), schedule.getMessage().getText());
        setNextRunDate(schedule);
    }

    public void setNextRunDate(Schedule schedule) {
        if (schedule.getSchedulerInterval() == SchedulerInterval.MINUTE) {
            // 60-1 fixes a scheduling problem where the localtime would be less than the run time by a few milliseconds,hence not printing the message
            // by rescheduling it for 59 seconds the localtime is always going to be ~1s more than the scheduled run time,making sure the message prints
            schedule.setNextRun(LocalDateTime.now().plusSeconds(300));
        }
        if (schedule.getSchedulerInterval() == SchedulerInterval.HOUR) {
            schedule.setNextRun(LocalDateTime.now().plusHours(1));
        }
        if (schedule.getSchedulerInterval() == SchedulerInterval.DAY) {
            schedule.setNextRun(LocalDateTime.now().plusDays(1));
        }
        _scheduleRepository.save(schedule);
    }

    public void sendAtScheduledRunDate(Schedule schedule) throws SlackApiException, IOException {
        if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
            slackService.postMessage(schedule.getChannel(), schedule.getMessage().getText());
            deactivateSchedule(schedule);
        }
    }

    public void deactivateSchedule(Schedule schedule) {
        if (!schedule.isRepeat()) schedule.setActive(false);
        setNextRunDate(schedule);
        _scheduleRepository.save(schedule);
    }
}
