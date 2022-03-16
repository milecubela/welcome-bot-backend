package com.nsoft.welcomebot.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "messages")
@ToString
@Getter
@Setter
@NoArgsConstructor
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


    @OneToMany(
            mappedBy = "message",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(
            mappedBy = "message",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Trigger> triggers = new ArrayList<>();

    public Message(String title, String text) {
        this.title = title;
        this.text = text;
    }
}
