package com.nsoft.welcomebot.Utilities;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public abstract class JSON {

    public static JsonObject get(String url) throws IOException {
        URL Url = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) Url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        JsonObject json = JsonParser.parseReader(in).getAsJsonObject();
        in.close();
        con.disconnect();
        return json;
    }
}
