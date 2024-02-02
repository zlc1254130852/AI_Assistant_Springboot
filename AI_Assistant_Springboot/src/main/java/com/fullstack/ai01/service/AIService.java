package com.fullstack.ai01.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public interface AIService {
    public String chat(int opt, String system, String question, String history, HttpServletResponse response, int[] abort);
}
