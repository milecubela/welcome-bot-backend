package com.nsoft.welcomebot.Models.RequestModels;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageRequest {
    @NonNull
    @Size(min = 5, max = 30)
    private String title;
    @NonNull
    @Size(min = 20)
    private String text;
}
