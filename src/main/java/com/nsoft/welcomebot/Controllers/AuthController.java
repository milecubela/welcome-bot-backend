package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestHeader("Authorization") TokenRequest tokenRequest) {
        return userService.loginUser(tokenRequest);
    }

}
