package com.nsoft.welcomebot.Services;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class OauthTokenService {

/*
    Function that takes googleid token as param, and checks if the token is valid
 */

    public JsonObject verifyGoogleToken(String token) throws IOException {

        URL url = new URL("https://oauth2.googleapis.com/tokeninfo?id_token=" + token);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
        in.close();
        con.disconnect();
        return jsonObject;
    }
}
