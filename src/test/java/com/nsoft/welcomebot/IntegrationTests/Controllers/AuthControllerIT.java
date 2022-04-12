package com.nsoft.welcomebot.IntegrationTests.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.Models.RequestModels.TokenRequest;
import com.nsoft.welcomebot.Models.RequestModels.UserRequest;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Security.AuthUtils.UserRole;
import com.nsoft.welcomebot.Services.OauthTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthControllerIT {

    private static final String EMAIL = "test-email@gmail.com";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Mock
    private OauthTokenService oauthTokenService;

    @BeforeEach
    void tearDown() {
        userRepository.deleteAll();
    }
// todo Mock google server to return 200 on token
//    @Test
//    void shouldReturnTokenAndRoleAfterSuccessfulLogin() throws Exception {
//        //given
//        UserRequest userRequest = new UserRequest(EMAIL);
//        User user = new User(userRequest);
//        user.setUserRole(UserRole.ADMIN);
//        userRepository.save(user);
//        TokenRequest tokenRequest = new TokenRequest("validaccesstoken");
//        String requestJson = objectMapper.writeValueAsString(tokenRequest);
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("email", EMAIL);
//        given(oauthTokenService.verifyGoogleToken(tokenRequest.getAccessToken())).willReturn(jsonObject);
//        //when
//        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson)
//                        .characterEncoding("utf-8"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        //then
//        String responseBody = result.getResponse().getContentAsString();
//        TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);
//        assertThat(tokenResponse.getAccessToken()).isEqualTo(tokenRequest.getAccessToken());
//        assertThat(tokenResponse.getUserRole()).isEqualTo(UserRole.ADMIN);
//    }

    @Test
    void shouldReturnBadRequestIfTokenDoesntValidateOnGoogleSide() throws Exception {
        //given
        UserRequest userRequest = new UserRequest(EMAIL);
        User user = new User(userRequest);
        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);
        TokenRequest tokenRequest = new TokenRequest("validaccesstoken");
        String requestJson = objectMapper.writeValueAsString(tokenRequest);
        //when
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
