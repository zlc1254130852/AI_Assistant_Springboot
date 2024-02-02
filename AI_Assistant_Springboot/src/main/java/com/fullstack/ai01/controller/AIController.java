package com.fullstack.ai01.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fullstack.ai01.service.AIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class AIController {

    public int[] abort={0,0};
    @Resource
    private AIService aiService;
    @GetMapping("/abort")
    public void abortStream(int option){
       abort[option]=1;
    }

    @PostMapping("/chat")
    public String chatCompletions(HttpServletRequest request, HttpServletResponse response){
        //abort[0]=0;
        try{
            BufferedReader reader = request.getReader();
            String line = null;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine())!=null){
                sb.append(line);
            }
            String body = sb.toString();
            //System.out.println(body);
            JSONObject jsonObject = JSON.parseObject(body);
            int opt = jsonObject.getInteger("option");

            abort[opt]=0;

            String system = jsonObject.getString("system");
            String question = jsonObject.getString("question");
            String history = jsonObject.getString("history");
            String result = aiService.chat(opt, system, question, history, response, abort);
            return result;
        } catch (Exception e) {
        e.printStackTrace();
    }
        return "";
    }
}
