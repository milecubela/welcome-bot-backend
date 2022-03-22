package com.nsoft.welcomebot.Models.RequestModels;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nsoft.welcomebot.Utilities.SchedulerInterval;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduleRequest {

    @NonNull
    @JsonProperty("isRepeat")
    private boolean isRepeat;

    @NonNull
    @JsonProperty("isActive")
    private boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @NonNull
    private LocalDateTime runDate;

    @Enumerated(EnumType.ORDINAL)
    private SchedulerInterval schedulerInterval;

    @NonNull
    private String channel;

    @NonNull
    private Long messageId;
}
