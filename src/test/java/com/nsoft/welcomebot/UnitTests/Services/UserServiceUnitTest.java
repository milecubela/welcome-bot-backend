package com.nsoft.welcomebot.UnitTests.Services;

import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Security.AuthUtils.UserRole;
import com.nsoft.welcomebot.Services.OauthTokenService;
import com.nsoft.welcomebot.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    @Test
    void shouldValidateAndReturnUser() {
        //given
        User user = new User(1L, EMAIL, UserRole.ADMIN);
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        //when
        User result = userService.validateUser(EMAIL);
        //then
        assertThat(result.getEmail()).isEqualTo(EMAIL);
    }
}
