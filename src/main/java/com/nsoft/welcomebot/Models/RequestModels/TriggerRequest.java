package com.nsoft.welcomebot.Models.RequestModels;

import com.nsoft.welcomebot.Utilities.TriggerEvent;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TriggerRequest {

    @NonNull
    private String channel;

    private Boolean isActive;

    @NonNull
    private Long messageId;

    private TriggerEvent triggerEvent;
}
