package com.fullstack.ai01;

import okhttp3.*;

import java.io.IOException;

public class AITest {
    public static void main(String[] args){
        // call open ai api interface: 4
        chatCompetions();
    }

    //call https://api.openai.com/v1/chat/completions interface
    public static void chatCompetions(){
        String url="https://api.openai.com/v1/chat/completions";
        String json="{\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"messages\": [\n" +
                "      {\n" +
                "        \"role\": \"user\",\n" +
                "        \"content\": \"Hello!\"\n" +
                "      }\n" +
                "    ],\"max_tokens\":500,\"temperature\":0.5\n" +
                "  }";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType,json);

        Request request = new Request.Builder().post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "")
                .url(url).build();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

        try{
            Response response = okHttpClient.newCall(request).execute();
            System.out.println(response.body().string());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
