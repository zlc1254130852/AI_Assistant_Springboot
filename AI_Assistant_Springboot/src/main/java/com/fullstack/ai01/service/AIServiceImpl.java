package com.fullstack.ai01.service;

import java.io.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.*;

import okio.Buffer;
import okio.BufferedSource;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class AIServiceImpl implements AIService{
//    String tmp="      {\n" +
//            "        \"role\": \"system\",\n" +
//            "        \"content\": \""+"You are an AI assistant."+"\"\n" +
//            "      },\n";
    int counter=0;
    @Override
    public String chat(int opt, String system, String question, String history, HttpServletResponse response, int[] abort){
        JsonElement jsonElement = JsonParser.parseString(history);
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        String url="https://api.openai.com/v1/chat/completions";
        String json="{\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"messages\": [\n";
        json+="      {\n" +
                "        \"role\": \"system\",\n" +
                "        \"content\": \""+system+"\"\n" +
                "      },\n";;

        for (int i = 0; i < jsonArray.size()-1; i++) {
            String s = jsonArray.get(i).toString();
            json+=s+",";
        }

        json+=
                "      {\n" +
                "        \"role\": \"user\",\n" +
                "        \"content\": \""+question+"\"\n" +
                "      }\n" +
                "    ],\"stream\":true,\"max_tokens\":500,\"temperature\":0.5\n" +
                "  }";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType,json);

        Request request = new Request.Builder().post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("")
                .url(url).build();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS).build();

        String result = null;
        String content = null;

        try{
            Response resp = okHttpClient.newCall(request).execute();
            InputStream inputStream = resp.body().byteStream();

            Reader reader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.readLine();
            String str;

            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                while ((str = bufferedReader.readLine()) != null && abort[opt]==0) {
                    out.write(str.getBytes("utf-8"));
                    out.write("\n".getBytes("utf-8"));
                    Objects.requireNonNull(out).flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Objects.requireNonNull(out).flush();
                inputStream.close();
                out.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
}
