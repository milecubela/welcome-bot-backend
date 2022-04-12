package com.nsoft.welcomebot.IntegrationTests.Repositories;

import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.Models.RequestModels.UserRequest;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Security.AuthUtils.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void tearDown() {
        userRepository.deleteAll();
    }

    /**
     * Testing if our custom query finds user by his e-mail
     */
    @Test
    void shouldFindUserByEmail() {
        //given
        UserRequest userRequest = new UserRequest("test-email@gmail.com");
        User user = new User(userRequest);
        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);

        //when
        Optional<User> result = userRepository.findByEmail("test-email@gmail.com");

        //then
        assertThat(result.get().getEmail()).isEqualTo("test-email@gmail.com");
    }
}
