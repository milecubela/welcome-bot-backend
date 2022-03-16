package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    private ResponseEntity loginUser(@RequestHeader("Authorization") String idtoken) {
        return userService.loginUser(idtoken);
    }
}
