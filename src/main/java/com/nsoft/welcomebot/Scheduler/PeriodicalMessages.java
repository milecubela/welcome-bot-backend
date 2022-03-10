package com.nsoft.welcomebot.Scheduler;


import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.MessageRepository;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.SlackModule.SlackModel;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PeriodicalMessages {
    private final MessageRepository _messageRepository;
    private final ScheduleRepository _scheduleRepository;


    @Scheduled(fixedDelay = 60000)
    public void scheduleFixedDelayTask() {
        Optional<List<Schedule>> schedules = Optional.ofNullable(_scheduleRepository.findAllActiveSchedulesNative());
        try {
            SlackModel bot= new SlackModel();
            bot.sendScheduledMessage("every 60 seconds in africa a minute passes");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        }

       // System.out.println(schedules);
    }

}
