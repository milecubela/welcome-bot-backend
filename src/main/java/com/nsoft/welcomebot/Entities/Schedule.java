package com.nsoft.welcomebot.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue
    private Long scheduleId;

    private boolean isRepeat;

    private boolean isActive;

    private LocalDate createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime runDate;

    private LocalDateTime nextRun;

    @Enumerated(EnumType.ORDINAL)
    private SchedulerInterval schedulerInterval;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    public Schedule() {
    }

    public Schedule(Schedule sched) {
        this.scheduleId = sched.getScheduleId();
        this.isRepeat = sched.getIsRepeat();
        this.isActive = sched.getIsActive();
        this.createdAt = sched.getCreatedAt();
        this.runDate = sched.getRunDate();
        this.schedulerInterval = sched.getSchedulerInterval();
    }

    public Schedule(Message message) {
        this.message = message;
    }

    public Schedule(Boolean isRepeat, Boolean isActive, LocalDateTime runDate, SchedulerInterval schedulerInterval) {
        this.isRepeat = isRepeat;
        this.isActive = isActive;
        this.runDate = runDate;
        this.schedulerInterval = schedulerInterval;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isactive) {
        this.isActive = isactive;
    }

    public boolean getIsRepeat() {
        return this.isRepeat;
    }

    @Override
    public String toString() {
        return "Schedule{" + "scheduleId=" + scheduleId + ", is_repeat=" + isRepeat + ", is_active=" + isActive + ", created_at=" + createdAt + ", run_date=" + runDate + '}';
    }

    public LocalDateTime getNextRun() {
        return nextRun;
    }

    public void setNextRun(LocalDateTime next_run) {
        this.nextRun = next_run;
    }

    public void setCreatedAt(LocalDate created_at) {
        this.createdAt = created_at;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public SchedulerInterval getSchedulerInterval() {
        return schedulerInterval;
    }

    public void setSchedulerInterval(SchedulerInterval schedulerInterval) {
        this.schedulerInterval = schedulerInterval;
    }

    public LocalDateTime getRunDate() {
        return runDate;
    }

}
