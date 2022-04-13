package com.nsoft.welcomebot.Scheduler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Entities.Schedule;
import com.nsoft.welcomebot.Repositories.ScheduleRepository;
import com.nsoft.welcomebot.Services.SlackService;
import com.nsoft.welcomebot.Utilities.Credentials;
import com.nsoft.welcomebot.Utilities.JSON;
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
public class PeriodicalGifs {
    private final ScheduleRepository scheduleRepository;
    private final Credentials credentials;
    private final SlackService slackService;

    @Scheduled(fixedDelay = 30000)
    public void sendGifs() throws IOException, SlackApiException {
        Schedule gif = scheduleRepository.getById(1108L);
        if (!gif.isActive() || !LocalDateTime.now().isAfter(gif.getNextRun())) {
            return;
        }
        JsonObject jsonObject = JSON.get(credentials.getGiphyUrl());
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
        return random.ints(0, 50).findFirst().getAsInt();
    }
}
