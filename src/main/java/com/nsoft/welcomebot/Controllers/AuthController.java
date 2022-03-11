package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Utilities.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/validateToken")
public class AuthController {

    @Autowired
    private UserRepository _userRepository;

    @PostMapping
    public ApiResponse verifyToken() {
        return new ApiResponse(HttpStatus.OK, "SUCCESS");
    }
}
