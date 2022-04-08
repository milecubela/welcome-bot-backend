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

        URL url = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + token);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JsonObject jsonObject = JsonParser.parseReader(br).getAsJsonObject();
        br.close();
        con.disconnect();
        return jsonObject;
    }

    public void revokeGoogleToken(String token) throws  IOException{
        URL url = new URL("https://oauth2.googleapis.com/revoke?token=" + token);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Length" , "0");
        con.setRequestProperty("Accept", "*/*");
        con.setDoOutput(true);
        con.connect();

        con.getOutputStream().close();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        br.close();
        con.disconnect();
    }
}
