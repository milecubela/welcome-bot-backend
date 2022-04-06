package com.nsoft.welcomebot.Scheduler;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Services.SlackService;
import com.nsoft.welcomebot.Utilities.ConsumeJSON;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        // this list is composed of all active repeating schedules
        List<Schedule> startRunningList = new ArrayList<>();
        sendGifs();
        // sendAtScheduledRunDate checks if the run date is now
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

    public void sendAtScheduledRunDate(Schedule schedule) throws SlackApiException, IOException {
        if (LocalDateTime.now().isAfter(schedule.getNextRun())) {
            slackService.postMessage(schedule.getChannel(), schedule.getMessage().getText());
            deactivateSchedule(schedule);
        }
    }

    public void deactivateSchedule(Schedule schedule) {
        if (!schedule.isRepeat()) schedule.setActive(false);
        setNextRunDate(schedule);
        scheduleRepository.save(schedule);
    }

    public void sendGifs() throws IOException, SlackApiException {
        Schedule gif = scheduleRepository.getById(1108L);
        if (!gif.isActive() || !LocalDateTime.now().isAfter(gif.getNextRun())) {
            return;
        }
        JsonObject jsonObject = ConsumeJSON.getJSONObject(credentials.getGiphyUrl());
        JsonObject data = new Gson().fromJson(jsonObject, JsonObject.class);
        String receivedJson = data.get("data").getAsJsonArray().get(randomNumber()).getAsJsonObject().get("images").getAsJsonObject().get("downsized").getAsJsonObject().get("url").getAsString();
        Attachment attachment = new Attachment();
        attachment.setText("");
        attachment.setImageUrl(receivedJson);
        List<Attachment> listAttachments = new ArrayList<>();
        listAttachments.add(attachment);
        slackService.postMessageWithAttachment(gif.getChannel(), listAttachments);
        gif.setNextRun(LocalDateTime.now().plusHours(1));
        scheduleRepository.save(gif);
    }

    public int randomNumber() {
        Random random = new Random();
        return random.ints(0, 50)
                .findFirst()
                .getAsInt();
    }
}
