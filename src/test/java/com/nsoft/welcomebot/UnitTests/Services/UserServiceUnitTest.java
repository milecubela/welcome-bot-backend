package com.nsoft.welcomebot.UnitTests.Services;

import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.ExceptionHandlers.CustomExceptions.BadTokenException;
import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Models.RequestModels.UserRequest;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Security.AuthUtils.UserRole;
import com.nsoft.welcomebot.Services.OauthTokenService;
import com.nsoft.welcomebot.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityExistsException;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

//@SpringBootTest
@ExtendWith({MockitoExtension.class})
class UserServiceUnitTest {

    private static final String EMAIL = "test-email@gmail.com";
    @Mock
    private UserRepository userRepository;
    @Mock
    private OauthTokenService oauthTokenService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, oauthTokenService);
    }

    /**
     * Test if getUserByEmail returns the user if it's present in database
     */
    @Test
    void shouldValidateAndReturnUser() {
        //given
        User user = new User(1L, EMAIL, UserRole.ADMIN);
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        //when
        Optional<User> result = userService.getUserByEmail(EMAIL);
        //then
        assertThat(result.get().getEmail()).isEqualTo(EMAIL);
    }

    /**
     * Testing if getUserByEmail returns emtpy if user doesn't exist
     */
    @Test
    void shouldReturnEmptyIfUserDoesntExist(){
        //given
        User user = new User(1L, EMAIL, UserRole.ADMIN);
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        //when
        Optional<User> result = userService.getUserByEmail(EMAIL);
        //then
        assertThat(result).isEmpty();
    }

    /**
     * Testing if addUser() will throw exception when user with given email already exists
     */
    @Test
    void shouldThrowIfUserAlreadyExistsOnCreation(){
        //given
        User user = new User(1L, EMAIL, UserRole.ADMIN);
        UserRequest userRequest = new UserRequest(EMAIL);
        given(userService.getUserByEmail(EMAIL)).willReturn(Optional.of(user));
        //then
        assertThatThrownBy(() -> userService.addUser(userRequest)).isInstanceOf(EntityExistsException.class).hasMessageContaining("User with email : " + EMAIL + " already exists");
    }

    /**
     * Testing if loginUser() throws if we recieve bad token request
     */
    @Test
    void shouldThrowIfBadRequestTokenDoesntExist(){
        //given
        TokenRequest tokenRequest = new TokenRequest();
        //then
        assertThatThrownBy(() -> userService.loginUser(tokenRequest)).isInstanceOf(BadTokenException.class).hasMessageContaining("Bad Token request! Provide a bearer token");
    }
    /**
     * Testing if loginUser() throws if we didn't define user in database
     */
    @Test
    void shouldThrowIfUserDoesntExist() throws IOException {
        //given
        TokenRequest tokenRequest = new TokenRequest("accesstokenstring");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", EMAIL);
        given(oauthTokenService.verifyGoogleToken("accesstokenstring")).willReturn(jsonObject);
        //then
        assertThatThrownBy(() -> userService.loginUser(tokenRequest)).isInstanceOf(UsernameNotFoundException.class).hasMessageContaining("User doesn't exist in the database");
    }
    /**
     * Testing if logoutUser() throws if we recieve bad token request
     */
    @Test
    void shouldThrowIfBadRequestTokenDoesntExistOnLogout(){
        //given
        TokenRequest tokenRequest = new TokenRequest();
        //then
        assertThatThrownBy(() -> userService.logoutUser(tokenRequest)).isInstanceOf(BadTokenException.class).hasMessageContaining("Bad Token request! Provide a bearer token");
    }

    /**
     * Testing if getEmailFromToken() will throw if google rejects the token
     */
    @Test
    void shouldThrowIfInvalidGoogleTokenSent() throws IOException {
        //given
        String token = "accesstoken";
        given(oauthTokenService.verifyGoogleToken(token)).willThrow(IOException.class);
        //then
        assertThatThrownBy(() -> userService.getEmailFromToken(token)).isInstanceOf(BadTokenException.class).hasMessageContaining("Invalid Google Token!");
    }
}
