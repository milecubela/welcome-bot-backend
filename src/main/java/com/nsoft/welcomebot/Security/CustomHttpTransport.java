package com.nsoft.welcomebot.Security;


import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;

import java.io.IOException;

public class CustomHttpTransport extends HttpTransport {
    @Override
    protected LowLevelHttpRequest buildRequest(String s, String s1) throws IOException {
        return null;
    }
}
