package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody TokenRequest tokenRequest) {
        return new ResponseEntity<>(userService.loginUser(tokenRequest), HttpStatus.OK);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Object> logoutUser(@Valid @RequestBody TokenRequest tokenRequest) {
        return new ResponseEntity<>(userService.logoutUser(tokenRequest), HttpStatus.OK);
    }
}
