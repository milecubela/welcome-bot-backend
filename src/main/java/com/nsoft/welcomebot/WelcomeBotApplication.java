package com.nsoft.welcomebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ServletComponentScan
public class WelcomeBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WelcomeBotApplication.class, args);
    }

}
