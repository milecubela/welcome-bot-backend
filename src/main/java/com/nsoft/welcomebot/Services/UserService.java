package com.nsoft.welcomebot.Services;

import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.BadTokenException;
import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Models.ResponseModels.TokenResponse;
import com.nsoft.welcomebot.Repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OauthTokenService oauthTokenService;

    public UserService(UserRepository userRepository, OauthTokenService oauthTokenService) {
        this.userRepository = userRepository;
        this.oauthTokenService = oauthTokenService;
    }

    /*
    Search for user in the database, and return it in UserDetails format from spring security
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User with that email not found ", email)));
    }

    /*
      User lookup in the database
      Should return true for admins
     */
    public boolean validateUser(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            return user.isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    /*
    Initial check for frontend token, returns an appropriate response
    */

    public ResponseEntity<Object> loginUser(TokenRequest tokenRequest) {
        String email = null;
        String idtoken = tokenRequest.getIdtoken();
        if (idtoken != null && idtoken.startsWith("Bearer ")) {
            String token = idtoken.substring(7);
            try {
                JsonObject jsonObject = oauthTokenService.verifyGoogleToken(token);
                email = jsonObject.get("email").getAsString();
            } catch (IOException e) {
                String message = e.getMessage();
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new BadTokenException("Bad Token request! Provide a bearer token");
        }

        TokenResponse tokenResponse = new TokenResponse();
        if (validateUser(email)) {
            tokenResponse.setIdToken(idtoken.substring(7));
            // Accepted, return OK and token
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("User doesn't exist in the database");
        }
    }
}