package com.nsoft.welcomebot.Scheduler;


import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import com.slack.api.bolt.App;
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
    private final App bot2;

    @Scheduled(fixedRate = 30000)
    public void sendScheduledMessages() throws SlackApiException, IOException {
        List<Schedule> schedules = _scheduleRepository.findAllActiveSchedules();
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

    public void sendMessage(Schedule schedule) throws SlackApiException, IOException {
        bot2.client().chatPostMessage(r -> r.token("xoxb-3185202762819-3204736816567-dCo8NEYO6vH7HvF7H5GqQCZ4").channel("C037FSVSEJX").text(schedule.getMessage().getText()));
        setNextRunDate(schedule);
        _scheduleRepository.save(schedule);
    }

    public void setNextRunDate(Schedule schedule) {
        if (schedule.getSchedulerInterval() == SchedulerInterval.MINUTE) {
            // 60-1 fixes a scheduling problem where the localtime would be less than the run time by a few milliseconds,hence not printing the message
            // by rescheduling it for 59 seconds the localtime is always going to be ~1s more than the scheduled run time,making sure the message prints
            schedule.setNextRun(LocalDateTime.now().plusSeconds(60-1));
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

    public void sendAtScheduledRunDate(Schedule schedule) throws SlackApiException, IOException {
        if (!schedule.getIsActive()) {
            return;
        }

        if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
            bot2.client().chatPostMessage(r -> r.token("xoxb-3185202762819-3204736816567-dCo8NEYO6vH7HvF7H5GqQCZ4").channel("C037FSVSEJX").text(schedule.getMessage().getText()));
            if (!schedule.getIsRepeat()) schedule.setIsActive(false);
            setNextRunDate(schedule);
            _scheduleRepository.save(schedule);
        }
    }

    public void checkForLaggingSchedules(Schedule schedule) throws SlackApiException, IOException {
        if (schedule.getNextRun() != null) {
            if (LocalDateTime.now().isAfter(schedule.getNextRun())) sendMessage(schedule);
        }
    }
}
