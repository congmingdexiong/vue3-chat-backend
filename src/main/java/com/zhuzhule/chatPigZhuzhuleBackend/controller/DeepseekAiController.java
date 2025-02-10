package com.zhuzhule.chatPigZhuzhuleBackend.controller;

import com.alibaba.fastjson.JSON;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.Result;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.UserInput;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.WxResource;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek.Choice;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek.DeepseekResult;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class DeepseekAiController {

  private static final Logger logger = LoggerFactory.getLogger(DeepseekAiController.class);

  static final OkHttpClient HTTP_CLIENT =
      new OkHttpClient()
          .newBuilder()
          .connectTimeout(500, TimeUnit.SECONDS)
          .readTimeout(500, TimeUnit.SECONDS)
          .writeTimeout(500, TimeUnit.MILLISECONDS)
          .build();

  @Value("${deepSeekConfig.apiKey}")
  public String DEEP_SEEK_SECRET_KEY;

  @PostMapping(value = "/deepseek")
  public Result getAiData(
      @org.springframework.web.bind.annotation.RequestBody UserInput userMsg,
      HttpServletRequest request)
      throws Exception {
    Result result = new Result();
    HttpSession session = request.getSession();
    System.out.println(
        "-------------------------------------------start-------------------------------------------");
    System.out.println("user question->Deepseek Ai:" + userMsg.getUserMsg());
    logger.info("用户{}>>正在提问", ((WxResource) session.getAttribute("userStorage")).getNickname());
    MediaType mediaType = MediaType.parse("application/json");
    String modelType = "deepseek-chat";
    RequestBody body =
        RequestBody.create(
            mediaType,
            "{\n  \"messages\": [\n    {\n      \"content\": \"You are a helpful assistant\",\n      \"role\": \"system\"\n    },\n    {\n      \"content\": \""
                + userMsg.getUserMsg()
                + "\",\n      \"role\": \"user\"\n    }\n  ],\n  \"model\": \""
                + modelType
                + "\",\n  \"frequency_penalty\": 0,\n  \"max_tokens\": 2048,\n  \"presence_penalty\": 0,\n  \"response_format\": {\n    \"type\": \"text\"\n  },\n  \"stop\": null,\n  \"stream\": false,\n  \"stream_options\": null,\n  \"temperature\": 1,\n  \"top_p\": 1,\n  \"tools\": null,\n  \"tool_choice\": \"none\",\n  \"logprobs\": false,\n  \"top_logprobs\": null\n}");
    Request req =
        new Request.Builder()
            .url("https://api.deepseek.com/chat/completions")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer " + DEEP_SEEK_SECRET_KEY)
            .build();
    Response response = HTTP_CLIENT.newCall(req).execute();
    String responseData = response.body().string();
    String responseJsonString = JSON.parseObject(responseData).toJSONString();

    DeepseekResult resDeepSeekResult = JSON.parseObject(responseJsonString, DeepseekResult.class);
    Choice[] choices = resDeepSeekResult.getChoices();
    result.setId(resDeepSeekResult.getId());

    result.setResult(choices[0].getMessage().getContent());
    System.out.println("Deepseek AI answer:");
    System.out.println(result.getResult());

    return result;
  }
}
