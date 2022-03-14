package com.nsoft.welcomebot.Services;



import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Utilities.ApiResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
public class OauthTokenService {

    private final UserService userService;
    private final GoogleIdTokenVerifier verifier;

    @Autowired
    public OauthTokenService(UserService userService, GoogleIdTokenVerifier verifier){
        this.userService = userService;
        this.verifier = verifier;
    }

/*
    Function that takes googleid token as param, and checks if the token is valid

 */
    public ApiResponse verifyGoogleToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken = verifier.verify(token);
        if(googleIdToken != null) {
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();

            if(!userService.validateUser(email)){
                return new ApiResponse(HttpStatus.UNAUTHORIZED, "The user is not defined as admin");
            }
            return  new ApiResponse(HttpStatus.OK, "The user exists in database, and is admin");
        }
        return new ApiResponse(HttpStatus.BAD_REQUEST, "Token is not valid");
    }
}
