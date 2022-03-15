package com.nsoft.welcomebot.Controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.Json;
import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Services.OauthTokenService;
import com.nsoft.welcomebot.Utilities.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping(path = "/api/v1/auth/validateToken")
public class AuthController {

    private final OauthTokenService _oauthTokenService;

    @Autowired
    public AuthController(OauthTokenService oauthTokenService) {
        _oauthTokenService = oauthTokenService;
    }

/*
    Route for initial validation of google idtoken.

// */
//    @PostMapping
//    public ApiResponse verifyToken(@RequestHeader("Authorization") String idtoken) throws GeneralSecurityException, IOException {
//        return _oauthTokenService.verifyGoogleToken(idtoken);
//    }
}
