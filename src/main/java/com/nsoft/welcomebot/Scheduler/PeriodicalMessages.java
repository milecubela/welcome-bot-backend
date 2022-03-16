package com.nsoft.welcomebot.Scheduler;


import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.SlackModule.SlackModel;
import com.nsoft.welcomebot.Utilities.Schedulertime;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PeriodicalMessages {
    private final ScheduleRepository _scheduleRepository;
    SlackModel bot;

    {
        try {
            bot = new SlackModel();
        } catch (IOException | DeploymentException e) {
            e.printStackTrace();
        }
    }


    @Scheduled(fixedRate = 30000)
    public void sendScheduledMessages() throws ParseException {
        List<Schedule> schedules = _scheduleRepository.findAll();
        // Ova lista sadrzi sve active + repeating schedules
        List<Schedule> startrunninglist = new ArrayList<>();

        // setDefaultValues POSTAVLJA LAST RUN NA RUN DATE (default) +  sendAtScheduledRunDate PROVJERAVA JE LI TRENUTNO RUN DATE
        for (Schedule schedule : schedules) {
            setDefaultValues(schedule);
            sendAtScheduledRunDate(schedule);
            //   IZDVAJA SVE SCHEDULES KOJE TREBA REPEATAT U SEPARATE LISTU
            if (checkIsRepeating(schedule)) startrunninglist.add(schedule);
        }

        //   IZVRSAVA REPEAT
        for (Schedule schedule : startrunninglist) {
            if (schedule.getschedulertime() == Schedulertime.MINUTE) {
                if (LocalDateTime.now().isAfter(schedule.getNext_run())) {
                        sendMessage(schedule);
                }
            }
            if (schedule.getschedulertime() == Schedulertime.HOUR) {
                if (checkIsItTimeToSend(schedule)) {
                    sendMessage(schedule);
                }
            }
            if (schedule.getschedulertime() == Schedulertime.DAY) {
                if (checkIsItTimeToSend(schedule)) {
                    sendMessage(schedule);
                }
            }
            checkForLaggingSchedules(schedule);
        }
    }

    private boolean checkIsItTimeToSend(Schedule schedule) {
        return LocalDateTime.now().isEqual(schedule.getNext_run());
    }

    public boolean checkIsRepeating(Schedule schedule) {
        return schedule.getIs_active() && schedule.getIs_repeat();
    }

    public void sendMessage(Schedule schedule) {
        schedule.setLast_run(LocalDateTime.now());
        bot.sendScheduledMessage(schedule.getMessage().getText());
        setNextRunDate(schedule);
        _scheduleRepository.save(schedule);
    }

    public void setNextRunDate(Schedule schedule) {
        if (schedule.getschedulertime() == Schedulertime.MINUTE) {
            schedule.setNext_run(schedule.getNext_run().plusMinutes(1));
        }
        if (schedule.getschedulertime() == Schedulertime.HOUR) {
            schedule.setNext_run(LocalDateTime.now().plusHours(1));
        }
        if (schedule.getschedulertime() == Schedulertime.DAY) {
            schedule.setNext_run(LocalDateTime.now().plusDays(1));
        }
        _scheduleRepository.save(schedule);

    }

    public void setDefaultValues(Schedule schedule) throws ParseException {
        if (schedule.getLast_run() == null || schedule.getRunDateConverted().isAfter(schedule.getLast_run())) {
            schedule.setNext_run(schedule.getRunDateConverted());
            schedule.setLast_run(schedule.getRunDateConverted());
            _scheduleRepository.save(schedule);
        }
    }

    public void sendAtScheduledRunDate(Schedule schedule) throws ParseException {
        if (schedule.getIs_active()) {
            if (ChronoUnit.MINUTES.between(schedule.getRunDateConverted(), LocalDateTime.now()) == 0) {
                bot.sendScheduledMessage(schedule.getMessage().getText());
                if (!schedule.getIs_repeat()) schedule.setIs_active(false);
                setNextRunDate(schedule);
                schedule.setLast_run(LocalDateTime.now());
                _scheduleRepository.save(schedule);
            }
        }
    }

    public void checkForLaggingSchedules(Schedule schedule) {
        if (schedule.getNext_run() == null) {
            if (LocalDateTime.now().isAfter(schedule.getNext_run())) sendMessage(schedule);
        }
    }
}
