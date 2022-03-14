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

    @NonNull
    private String channel;

    private Boolean isActive;

    @ManyToOne()
    @JoinColumn(name = "message_id")
    private Message message;

    private TriggerEvent triggerEvent;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Trigger() {}

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
