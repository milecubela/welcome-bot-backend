package com.nsoft.welcomebot.Entities;

import com.nsoft.welcomebot.Utilities.TriggerEvent;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@Table(name = "triggers")
public class Trigger {

    @Id
    @GeneratedValue
    private Long triggerId;

    @ManyToOne
    @JoinColumn(name = "messageId", referencedColumnName = "messageId")
    private Message message;
    private TriggerEvent triggerEvent;

    @NonNull
    private String channel;

    public Trigger() {

    }

    @Override
    public String toString() {
        return "Trigger{" +
                "triggerId=" + triggerId +
                ", message=" + message +
                ", triggerEvent=" + triggerEvent +
                ", channel='" + channel + '\'' +
                '}';
    }

    public Long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public TriggerEvent getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(TriggerEvent triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
