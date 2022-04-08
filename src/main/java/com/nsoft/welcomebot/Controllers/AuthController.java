package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody TokenRequest tokenRequest) {
        return userService.loginUser(tokenRequest);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Object> logoutUser(@Valid @RequestBody TokenRequest tokenRequest){
        return userService.logoutUser(tokenRequest);
    }

}
