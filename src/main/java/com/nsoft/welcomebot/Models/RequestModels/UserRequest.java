package com.nsoft.welcomebot.Models.RequestModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {

    @NonNull
    @JsonProperty("email")
    @Email
    private String email;
}
