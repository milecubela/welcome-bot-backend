package com.nsoft.welcomebot.Entities;

import com.nsoft.welcomebot.Models.RequestModels.TriggerRequest;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "triggers")
public class Trigger {

    @Id
    @GeneratedValue
    private Long triggerId;

    @NonNull
    private String channel;

    private Boolean isActive;

    @ManyToOne()
    @JoinColumn(name = "message_id")
    private Message message;

    private TriggerEvent triggerEvent;

    public Trigger(TriggerRequest triggerRequest) {
        this.channel = triggerRequest.getChannel();
        this.triggerEvent = triggerRequest.getTriggerEvent();
        this.isActive = triggerRequest.getIsActive();
    }
}
