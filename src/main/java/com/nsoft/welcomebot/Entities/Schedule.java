package com.nsoft.welcomebot.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nsoft.welcomebot.Models.RequestModels.ScheduleRequest;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue
    private Long scheduleId;

    @NonNull
    private boolean isRepeat;

    @NonNull
    private boolean isActive;

    private LocalDate createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @NonNull
    private LocalDateTime runDate;

    private LocalDateTime nextRun;

    @Enumerated(EnumType.ORDINAL)
    private SchedulerInterval schedulerInterval;

    @ManyToOne
    @JoinColumn(name = "message_id")
    @NonNull
    private Message message;

    @NonNull
    private String channel;

    public Schedule(ScheduleRequest scheduleRequest) {
        this.isRepeat = scheduleRequest.isRepeat();
        this.isActive = scheduleRequest.isActive();
        this.runDate = scheduleRequest.getRunDate();
        this.schedulerInterval = scheduleRequest.getSchedulerInterval();
        this.channel = scheduleRequest.getChannel();
        this.nextRun = scheduleRequest.getRunDate();
    }

    public void updateSchedule(ScheduleRequest scheduleRequest) {
        this.isRepeat = scheduleRequest.isRepeat();
        this.isActive = scheduleRequest.isActive();
        this.runDate = scheduleRequest.getRunDate();
        this.schedulerInterval = scheduleRequest.getSchedulerInterval();
        this.channel = scheduleRequest.getChannel();
        this.nextRun = scheduleRequest.getRunDate();
    }
}
