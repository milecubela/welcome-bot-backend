package com.nsoft.welcomebot.Services;



import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nsoft.welcomebot.Entities.User;
import com.nsoft.welcomebot.Repositories.UserRepository;
import com.nsoft.welcomebot.Utilities.ApiResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
public class OauthTokenService {

/*
    Function that takes googleid token as param, and checks if the token is valid
 */

    public JsonObject verifyGoogleToken(String token) throws GeneralSecurityException, IOException {

        URL url = new URL("https://oauth2.googleapis.com/tokeninfo?id_token=" + token);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        JsonObject json = JsonParser.parseReader(in).getAsJsonObject();
        in.close();
        con.disconnect();
        System.out.println(json);
        return json;
    }
}
