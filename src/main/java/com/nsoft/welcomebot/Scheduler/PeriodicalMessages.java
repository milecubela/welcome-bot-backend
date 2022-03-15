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
        for (Schedule i : schedules) {
            setDefaultValues(i);
            sendAtScheduledRunDate(i);
            //   IZDVAJA SVE SCHEDULES KOJE TREBA REPEATAT U SEPARATE LISTU
            if (checkIsRepeating(i)) startrunninglist.add(i);
        }

        //   IZVRSAVA REPEAT
        for (Schedule a : startrunninglist) {
            if (a.getschedulertime() == Schedulertime.MINUTE) {
                System.out.println("current time " + LocalDateTime.now());
                System.out.println("schedul time " + a.getNext_run());
                if (LocalDateTime.now().isAfter(a.getNext_run())) {
                        sendMessage(a);
                }
            }
            if (a.getschedulertime() == Schedulertime.HOUR) {
                if (checkIsItTimeToSend(a)) {
                    sendMessage(a);
                }
            }
            if (a.getschedulertime() == Schedulertime.DAY) {
                if (checkIsItTimeToSend(a)) {
                    sendMessage(a);
                }
            }
            checkForLaggingSchedules(a);
        }
    }

    private boolean checkIsItTimeToSend(Schedule a) {
        return LocalDateTime.now().isEqual(a.getNext_run());
    }

    public boolean checkIsRepeating(Schedule a) {
        return a.getIs_active() && a.getIs_repeat();
    }

    public void sendMessage(Schedule s) {
        s.setLast_run(LocalDateTime.now());
        bot.sendScheduledMessage(s.getMessage().getText());
        setNextRunDate(s);
        _scheduleRepository.save(s);
    }

    public void setNextRunDate(Schedule s) {
        if (s.getschedulertime() == Schedulertime.MINUTE) {
            s.setNext_run(s.getNext_run().plusMinutes(1));
        }
        if (s.getschedulertime() == Schedulertime.HOUR) {
            s.setNext_run(LocalDateTime.now().plusHours(1));
        }
        if (s.getschedulertime() == Schedulertime.DAY) {
            s.setNext_run(LocalDateTime.now().plusDays(1));
        }
        _scheduleRepository.save(s);

    }

    public void setDefaultValues(Schedule i) throws ParseException {
        if (i.getLast_run() == null || i.getRunDateConverted().isAfter(i.getLast_run())) {
            i.setNext_run(i.getRunDateConverted());
            i.setLast_run(i.getRunDateConverted());
            _scheduleRepository.save(i);
        }
    }

    public void sendAtScheduledRunDate(Schedule i) throws ParseException {
        if (i.getIs_active()) {
            if (ChronoUnit.MINUTES.between(i.getRunDateConverted(), LocalDateTime.now()) == 0) {
                bot.sendScheduledMessage(i.getMessage().getText());
                if (!i.getIs_repeat()) i.setIs_active(false);
                setNextRunDate(i);
                i.setLast_run(LocalDateTime.now());
                _scheduleRepository.save(i);
            }
        }
    }

    public void checkForLaggingSchedules(Schedule a) {
        if (a.getNext_run() == null) {
            if (LocalDateTime.now().isAfter(a.getNext_run())) sendMessage(a);
        }
    }
}
