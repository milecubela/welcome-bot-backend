package com.nsoft.welcomebot.Services;

import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.BadTokenException;
import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Models.RequestModels.UserRequest;
import com.nsoft.welcomebot.Models.ResponseModels.TokenResponse;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Security.AuthUtils.UserRole;
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
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUser(UserRequest userRequest) {
        if (getUserByEmail(userRequest.getEmail()).isPresent()) {
            throw new EntityExistsException("User with email : " + userRequest.getEmail() + " already exists");
        }
        User user = new User(userRequest);
        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);
    }

    /**
     * Initial check for frontend token, returns an appropriate response
     */

    public TokenResponse loginUser(TokenRequest tokenRequest) {
        String accessToken = tokenRequest.getAccessToken();

        if (accessToken == null) {
            throw new BadTokenException("Bad Token request! Provide a bearer token");
        }
        String email = getEmailFromToken(accessToken);
        Optional<User> user = getUserByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User doesn't exist in the database");
        }

        // Accepted, token response with token and user role
        return new TokenResponse(accessToken, user.get().getUserRole());
    }

    public String logoutUser(TokenRequest tokenRequest) {
        String accessToken = tokenRequest.getAccessToken();
        if (accessToken == null) {
            throw new BadTokenException("Bad Token request! Provide a bearer token");
        }
        try {
            oauthTokenService.revokeGoogleToken(accessToken);
        } catch (IOException e) {
            throw new BadTokenException("Invalid google token or token is already revoked. Please provide a valid token");
        }
        return "Successfully logged out user!";
    }

    public String getEmailFromToken(String token) {
        String email;
        try {
            JsonObject jsonObject = oauthTokenService.verifyGoogleToken(token);
            email = jsonObject.get("email").getAsString();
        } catch (IOException e) {
            throw new BadTokenException("Invalid Google Token!");
        }
        return email;
    }
}
