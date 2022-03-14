package com.nsoft.welcomebot.Scheduler;


import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.SlackModule.SlackModel;
import com.nsoft.welcomebot.Utilities.Schedulertime;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class PeriodicalMessages {
    private final MessageRepository _messageRepository;
    private final ScheduleRepository _scheduleRepository;
//    SlackModel bot;
//    {
//        try {
//            bot = new SlackModel();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DeploymentException e) {
//            e.printStackTrace();
//        }
//    }


    @Scheduled(fixedRate = 60000)
    public void scheduleFixedDelayTask() throws ParseException {
        List<Schedule> schedules = _scheduleRepository.findAll();
        // Ova lista sadrzi sve active + repeating schedules
        List<Schedule> startrunninglist = new ArrayList<Schedule>();

        // (prvi if) POSTAVLJA LAST RUN NA RUN DATE (default) +  (drugi if() )PROVJERAVA JE LI TRENUTNO RUN DATE
        for (Schedule i: schedules) {
            if (i.getLast_run()==null || i.getRunDateConverted().isAfter(i.getLast_run())){
//                Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(i.getRun_date());
//                var temp2 = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                i.setNext_run(i.getRunDateConverted());
                i.setLast_run(i.getRunDateConverted());
                _scheduleRepository.save(i);
            }
            if(i.getIs_active()==true){
                Date rundate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(i.getRun_date());
                if (ChronoUnit.MINUTES.between(rundate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),LocalDateTime.now())==0){
                    botsendmessage(i.getMessage());
//                    bot.sendScheduledMessage(i.getMessage().getText());
                    System.out.println("difference between dates is : "+ChronoUnit.MINUTES.between(rundate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),LocalDateTime.now()));
                    if (i.getIs_repeat()==false){ i.setIs_active(false);}
                    i.setLast_run(LocalDateTime.now());
                    _scheduleRepository.save(i);
                }
            }
        }

        //   IZDVAJA SVE SCHEDULES KOJE TREBA REPEATAT U SEPARATE LISTU
        for (Schedule i: schedules) {
            if( i.getIs_active()==true && i.getIs_repeat()==true ){startrunninglist.add(i);}
        }

        //   IZVRSAVA REPEAT
        for (Schedule a:startrunninglist) {
            if (a.getschedulertime()== Schedulertime.MINUTE){
                if (a.getIs_active()==true && a.getIs_repeat()==true /*&& ChronoUnit.SECONDS.between(a.getLast_run().minusSeconds(1),LocalDateTime.now())>=58*/){
                    a.setLast_run(LocalDateTime.now());
                    a.setNext_run(LocalDateTime.now().plusMinutes(1));
                    botsendmessage(a.getMessage());
//                    bot.sendScheduledMessage(a.getMessage().getText());
                    _scheduleRepository.save(a);
                }
            }
            if (a.getschedulertime()== Schedulertime.HOUR){
                if (a.getIs_active()==true && a.getIs_repeat()==true && ChronoUnit.MINUTES.between(a.getLast_run(),LocalDateTime.now())>=60){
                a.setLast_run(LocalDateTime.now());
                a.setNext_run(LocalDateTime.now().plusHours(1));
                botsendmessage(a.getMessage());
//                bot.sendScheduledMessage(a.getMessage().getText());
                _scheduleRepository.save(a);
            }}
            if (a.getschedulertime()== Schedulertime.DAY){
                if (a.getIs_active()==true && a.getIs_repeat()==true && ChronoUnit.HOURS.between(a.getLast_run(),LocalDateTime.now())>=24){
                a.setLast_run(LocalDateTime.now());
                a.setNext_run(LocalDateTime.now().plusDays(1));
                botsendmessage(a.getMessage());
//                bot.sendScheduledMessage(a.getMessage().getText());
                _scheduleRepository.save(a);
            }}
        }
    }
    public void botsendmessage(Message msg){
        try {
            SlackModel bot= new SlackModel();
            bot.sendScheduledMessage(msg.getText());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }
}
