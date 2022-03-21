package com.nsoft.welcomebot.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nsoft.welcomebot.Models.RequestModels.MessageRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private Long messageId;

    @NonNull
    @Size(min = 5, max = 30)
    private String title;

    @NonNull
    @Size(min = 20)
    private String text;

    private LocalDate createdAt;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Trigger> triggers = new ArrayList<>();

    public Message(MessageRequest messageRequest) {
        this.text = messageRequest.getText();
        this.title = messageRequest.getTitle();
    }
}
