package com.nsoft.welcomebot.Models.RequestModels;

import com.nsoft.welcomebot.Entities.Message;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TriggerRequest {

    @NonNull
    private String channel;

    private Boolean isActive;

    @ManyToOne()
    @JoinColumn(name = "message_id")
    private Message message;

    private TriggerEvent triggerEvent;
}
