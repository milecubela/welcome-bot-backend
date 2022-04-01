package com.nsoft.welcomebot.Services;

import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.BadTokenException;
import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Models.RequestModels.UserRequest;
import com.nsoft.welcomebot.Models.ResponseModels.TokenResponse;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Security.AuthUtils.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
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
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
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

    public void addUser(UserRequest userRequest) {
        if (validateUser(userRequest.getEmail())) {
            throw new EntityExistsException("User with email : " + userRequest.getEmail() + " already exists");
        }
        User user = new User(userRequest);
        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);
    }

    /**
     *  Initial check for frontend token, returns an appropriate response
    */

    public ResponseEntity<Object> loginUser(TokenRequest tokenRequest) {
        String idToken = tokenRequest.getIdToken();

        if (idToken == null || !idToken.startsWith("Bearer ")) {
            throw new BadTokenException("Bad Token request! Provide a bearer token");
        }
        String email = getEmailFromToken(idToken.substring(7));

        if (!validateUser(email)) {
            throw new UsernameNotFoundException("User doesn't exist in the database");
        }

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setIdToken(idToken.substring(7));
        // Accepted, return OK and token
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    private String getEmailFromToken(String token) {
        String email;
        try {
            JsonObject jsonObject = oauthTokenService.verifyGoogleToken(token);
            email = jsonObject.get("email").getAsString();
        } catch (IOException e) {
            throw new BadTokenException(e.getMessage());
        }
        return email;
    }
}
