package com.nsoft.welcomebot.Scheduler;


import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Services.SlackService;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import com.slack.api.methods.SlackApiException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
@AllArgsConstructor
public class PeriodicalMessages {
    private final ScheduleRepository scheduleRepository;
    private final Credentials credentials;
    private final SlackService slackService;

    @Scheduled(fixedDelay = 30000)
    public void sendScheduledMessages() throws SlackApiException, IOException {
        List<Schedule> schedules = scheduleRepository.findAllActiveSchedules();
        for (Schedule schedule : schedules) {
            if (!LocalDateTime.now().isAfter(schedule.getNextRun())) {
                continue;
            }
            sendAtScheduledRunDateAndDeactivate(schedule);
            if (isRepeating(schedule)) setNextRunDate(schedule);
        }
    }

    public boolean isRepeating(Schedule schedule) {
        return schedule.isActive() && schedule.isRepeat();
    }

    public void setNextRunDate(Schedule schedule) {
        if (schedule.getSchedulerInterval() == SchedulerInterval.MINUTE) {
            schedule.setNextRun(LocalDateTime.now().plusSeconds(300));
        }
        if (schedule.getSchedulerInterval() == SchedulerInterval.HOUR) {
            schedule.setNextRun(LocalDateTime.now().plusHours(1));
        }
        if (schedule.getSchedulerInterval() == SchedulerInterval.DAY) {
            schedule.setNextRun(LocalDateTime.now().plusDays(1));
        }
        scheduleRepository.save(schedule);
    }

    public void sendAtScheduledRunDateAndDeactivate(Schedule schedule) throws SlackApiException, IOException {
        slackService.postMessage(schedule.getChannel(), schedule.getMessage().getText());
        deactivateSchedule(schedule);
    }

    public void deactivateSchedule(Schedule schedule) {
        if (!schedule.isRepeat()) schedule.setActive(false);
        setNextRunDate(schedule);
        scheduleRepository.save(schedule);
    }
}
