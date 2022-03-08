package com.nsoft.welcomebot.Entities;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NonNull;
import org.aspectj.apache.bcel.classfile.Module;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Size(min = 5,max = 30)
    private String title;

    @NonNull
    @Size(min = 20)
    private String text;

    private LocalDate createdAt;
//    private LocalDate createdAt = LocalDate.now();

    public Message() {
    }

    public Message(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
