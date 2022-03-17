package com.nsoft.welcomebot.Scheduler;


import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.SlackModule.SlackModel;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PeriodicalMessages {
    private final ScheduleRepository _scheduleRepository;
    private final SlackModel bot;

    @Scheduled(fixedRate = 30000)
    public void sendScheduledMessages() {
        List<Schedule> schedules = _scheduleRepository.findAll();
        // this list is composed of all active repeating schedules
        List<Schedule> startrunninglist = new ArrayList<>();

        // setDefaultValues sets next run to run date value  (default) +  sendAtScheduledRunDate checks if the run date is now
        for (Schedule schedule : schedules) {
            setDefaultValues(schedule);
            sendAtScheduledRunDate(schedule);
            //   separates all repeating schedules
            if (checkIsRepeating(schedule)) startrunninglist.add(schedule);
        }

        //   repeating
        for (Schedule schedule : startrunninglist) {
            if (schedule.getSchedulerInterval() == SchedulerInterval.MINUTE) {
                if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
                    sendMessage(schedule);
                }
            }
            if (schedule.getSchedulerInterval() == SchedulerInterval.HOUR) {
                if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
                    sendMessage(schedule);
                }
            }
            if (schedule.getSchedulerInterval() == SchedulerInterval.DAY) {
                if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
                    sendMessage(schedule);
                }
            }
            checkForLaggingSchedules(schedule);
        }
    }

    public boolean checkIsRepeating(Schedule schedule) {
        return schedule.getIsActive() && schedule.getIsRepeat();
    }

    public void sendMessage(Schedule schedule) {
        bot.sendScheduledMessage(schedule.getMessage().getText());
        setNextRunDate(schedule);
        _scheduleRepository.save(schedule);
    }

    public void setNextRunDate(Schedule schedule) {
        if (schedule.getSchedulerInterval() == SchedulerInterval.MINUTE) {
            schedule.setNextRun(schedule.getNextRun().plusMinutes(1));
        }
        if (schedule.getSchedulerInterval() == SchedulerInterval.HOUR) {
            schedule.setNextRun(LocalDateTime.now().plusHours(1));
        }
        if (schedule.getSchedulerInterval() == SchedulerInterval.DAY) {
            schedule.setNextRun(LocalDateTime.now().plusDays(1));
        }
        _scheduleRepository.save(schedule);
    }

    public void setDefaultValues(Schedule schedule) {
        if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
            schedule.setNextRun(schedule.getNextRun());
            _scheduleRepository.save(schedule);
        }
    }

    public void sendAtScheduledRunDate(Schedule schedule) {
        if (!schedule.getIsActive()) {
            return;
        }

        if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
            bot.sendScheduledMessage(schedule.getMessage().getText());
            if (!schedule.getIsRepeat()) schedule.setIsActive(false);
            setNextRunDate(schedule);
            _scheduleRepository.save(schedule);
        }
    }

    public void checkForLaggingSchedules(Schedule schedule) {
        if (schedule.getNextRun() != null) {
            if (LocalDateTime.now().isAfter(schedule.getNextRun())) sendMessage(schedule);
        }
    }
}
