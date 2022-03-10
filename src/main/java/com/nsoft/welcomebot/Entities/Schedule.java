package com.nsoft.welcomebot.Entities;

import com.nsoft.welcomebot.Utilities.RepeatingValue;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

import static com.nsoft.welcomebot.Utilities.TriggerEvent.CHANNEL_LEFT;

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

    private Date run_date;
//    @Enumerated
//    private RepeatingValue repeat;

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
    }

    public Schedule(Message message) {
        this.message = message;
    }

    public Schedule(Boolean is_repeat, Boolean is_active, Date run_date /*, RepeatingValue repeat*/) {
        this.is_repeat = is_repeat;
        this.is_active = is_active;
        this.run_date = run_date;
//        this.repeat = repeat;
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

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


}
