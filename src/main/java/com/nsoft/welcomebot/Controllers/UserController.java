package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Models.RequestModels.UserRequest;
import com.nsoft.welcomebot.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<Object> addAdmin(@Valid @RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return new ResponseEntity<>("Created new admin successfully", HttpStatus.CREATED);
    }
}
