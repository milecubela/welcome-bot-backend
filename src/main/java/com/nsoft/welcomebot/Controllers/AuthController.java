package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Models.RequestModels.UserRequest;
import com.nsoft.welcomebot.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestHeader("Authorization") TokenRequest tokenRequest) {
        return userService.loginUser(tokenRequest);
    }

    @PostMapping(path = "/addAdmin")
    public ResponseEntity<Object> addAdmin(@Valid @RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return new ResponseEntity<>("Created new user successfully", HttpStatus.CREATED);
    }

}
