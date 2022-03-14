package com.nsoft.welcomebot.Security;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;

import java.io.*;
import java.nio.charset.Charset;

public class CustomJsonFactory extends JsonFactory {
    @Override
    public JsonParser createJsonParser(InputStream inputStream) throws IOException {
        return null;
    }

    @Override
    public JsonParser createJsonParser(InputStream inputStream, Charset charset) throws IOException {
        return null;
    }

    @Override
    public JsonParser createJsonParser(String s) throws IOException {
        return null;
    }

    @Override
    public JsonParser createJsonParser(Reader reader) throws IOException {
        return null;
    }

    @Override
    public JsonGenerator createJsonGenerator(OutputStream outputStream, Charset charset) throws IOException {
        return null;
    }

    @Override
    public JsonGenerator createJsonGenerator(Writer writer) throws IOException {
        return null;
    }
}
