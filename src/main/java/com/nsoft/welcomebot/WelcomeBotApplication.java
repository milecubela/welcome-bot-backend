package com.nsoft.welcomebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
@EnableSwagger2
@ServletComponentScan
@EnableScheduling
public class WelcomeBotApplication {

    public static void main(String[] args) throws DeploymentException, IOException {
        SpringApplication.run(WelcomeBotApplication.class, args);
    }

}
