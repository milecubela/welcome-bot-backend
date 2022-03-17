package com.nsoft.welcomebot.Entities;

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
}
