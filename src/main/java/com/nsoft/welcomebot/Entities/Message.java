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
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getText() {
//        return text;
//    }
//
////    public void setText(String text) {
////        this.text = text;
////    }
//
//    public LocalDate getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDate createdAt) {
//        this.createdAt = createdAt;
//    }
//
// /*    @Override
//   public String toString() {
//        return "Message{" +
//                "id=" + messageId +
//                ", title='" + title + '\'' +
//                ", text='" + text + '\'' +
//                ", createdAt=" + createdAt +
//                '}';
//    }*/
//
//    public Message(List<Schedule> schedules) {
//        this.schedules = schedules;
//    }
//
//    public void setSchedules(List<Schedule> schedules) {
//        this.schedules = schedules;
//    }

}
