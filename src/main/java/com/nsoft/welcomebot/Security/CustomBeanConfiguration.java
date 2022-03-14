package com.nsoft.welcomebot.Security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;

@Configuration
public class CustomBeanConfiguration {

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(HttpTransport httpTransport, JsonFactory jsonFactory){
        return new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singleton("808924355914-r7km6h2s0l7jcqh826ca9u7vcsj4rr9l.apps.googleusercontent.com"))
                .build();
    }

    @Bean
    public HttpTransport httpTransport() {
        return new CustomHttpTransport() {

        };
    }

    @Bean
    public JsonFactory jsonFactory(){
        return new CustomJsonFactory() {

        };
    }
}