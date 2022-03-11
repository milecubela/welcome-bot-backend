package com.nsoft.welcomebot.Controllers;

import com.nsoft.welcomebot.Repositories.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/validateToken")
public class AuthController {

    private UserRepository _userRepository;
}
