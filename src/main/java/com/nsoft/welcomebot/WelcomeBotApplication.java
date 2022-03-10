package com.nsoft.welcomebot;

import com.nsoft.welcomebot.SlackModule.SlackModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
public class WelcomeBotApplication {

    public static void main(String[] args) throws DeploymentException, IOException {
        SpringApplication.run(WelcomeBotApplication.class, args);
    }

}
