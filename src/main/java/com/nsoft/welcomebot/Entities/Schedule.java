package com.nsoft.welcomebot.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String channel;
}
