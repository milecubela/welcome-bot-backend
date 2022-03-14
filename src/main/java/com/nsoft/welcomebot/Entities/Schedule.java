package com.nsoft.welcomebot.Entities;

import com.nsoft.welcomebot.Utilities.Schedulertime;
import lombok.Data;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue
    private Long scheduleId;

    private Boolean is_repeat;

    private Boolean is_active;

    private LocalDate created_at;

    private String run_date;

    private LocalDateTime last_run;


    private LocalDateTime next_run;

    @Enumerated(EnumType.ORDINAL)
    private Schedulertime schedulertime;

    // @ManyToOne
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    public Schedule() {
    }

    public Schedule(Schedule sched) {
        this.scheduleId = sched.getScheduleId();
        this.is_repeat = sched.getIs_repeat();
        this.is_active = sched.getIs_active();
        this.created_at = sched.getCreated_at();
        this.run_date = sched.getRun_date();
        this.schedulertime = sched.getschedulertime();
    }

    public Schedule(Message message) {
        this.message = message;
    }

    public Schedule(Boolean is_repeat, Boolean is_active, String run_date , Schedulertime schedulertime) {
        this.is_repeat = is_repeat;
        this.is_active = is_active;
        this.run_date = run_date;
        this.schedulertime = schedulertime;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", is_repeat=" + is_repeat +
                ", is_active=" + is_active +
                ", created_at=" + created_at +
                ", run_date=" + run_date +
                '}';
    }

    public LocalDateTime getNext_run() {
        return next_run;
    }

    public void setNext_run(LocalDateTime next_run) {
        this.next_run = next_run;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Schedulertime getschedulertime() {
        return schedulertime;
    }

    public void setSchedulertime(Schedulertime schedulertime) {
        this.schedulertime = schedulertime;
    }

    public LocalDateTime getLast_run() {
        return last_run;
    }
    public LocalDateTime getRunDateConverted() throws ParseException {
        // PRETVARA STRING OBJEKAT U DATUM I VRACA LOCALDATETIME
        Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getRun_date());
        var temp2 = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return temp2;
    }
    public void setLast_run(LocalDateTime last_run) {
        this.last_run = last_run;
    }

}
