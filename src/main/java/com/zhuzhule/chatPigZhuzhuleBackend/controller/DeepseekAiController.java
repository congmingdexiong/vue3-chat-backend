package com.zhuzhule.chatPigZhuzhuleBackend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.ChatContent;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.Conversation;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.Result;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.WxResource;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek.Choice;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek.DeepseekResult;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek.RequestPayload;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ChatContentService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class DeepseekAiController {

  private static final Logger logger = LoggerFactory.getLogger(DeepseekAiController.class);

  @Autowired private ChatContentService chatContentService;

  static final OkHttpClient HTTP_CLIENT =
      new OkHttpClient()
          .newBuilder()
          .connectTimeout(180000, TimeUnit.SECONDS)
          .readTimeout(180000, TimeUnit.SECONDS)
          .writeTimeout(180000, TimeUnit.MILLISECONDS)
          .build();

  @Value("${deepSeekConfig.apiKey}")
  public String DEEP_SEEK_SECRET_KEY;

  @PostMapping(value = "/deepseek")
  public Result getAiData(
      @org.springframework.web.bind.annotation.RequestBody RequestPayload requestPayload,
      HttpServletRequest request)
      throws Exception {
    Result result = new Result();
    String userMsg = requestPayload.getUserMsg();
    Boolean enabledReasoner = requestPayload.getOpts().getEnabledReasoner();
    HttpSession session = request.getSession();
    System.out.println(
        "-------------------------------------------start-------------------------------------------");
    String modelType = enabledReasoner.equals(true) ? "deepseek-reasoner" : "deepseek-chat";
    System.out.println("user question->Deepseek Ai(\"" + modelType + "\"):" + userMsg);
    logger.info("用户{}>>正在提问", ((WxResource) session.getAttribute("userStorage")).getNickname());
    MediaType mediaType = MediaType.parse("application/json");
    Conversation conversation = (Conversation) session.getAttribute("activeConversation");
    List<ChatContent> chatContents =
        chatContentService.getChatContentByConversationId(conversation.getId());
    String inputStr = this.getJSONString(userMsg, chatContents, modelType);
    RequestBody body = RequestBody.create(mediaType, inputStr);
    Request req =
        new Request.Builder()
            .url("https://api.deepseek.com/chat/completions")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer " + DEEP_SEEK_SECRET_KEY)
            .build();
    try {
      Response response = HTTP_CLIENT.newCall(req).execute();
      String responseData = response.body().string();
      String responseJsonString = JSON.parseObject(responseData).toJSONString();

      DeepseekResult resDeepSeekResult = JSON.parseObject(responseJsonString, DeepseekResult.class);
      Choice[] choices = resDeepSeekResult.getChoices();
      result.setId(resDeepSeekResult.getId());
      result.setConversation((Conversation) session.getAttribute("activeConversation"));
      result.setResult(choices[0].getMessage().getContent());
      System.out.println("Deepseek AI answer:");
      System.out.println(result.getResult());
    } catch (Error error) {
      logger.info("error:{}", error);
      result.setResult("服务器正在忙碌，请重试！");
    } finally {
      return result;
    }
  }

  public String getJSONString(String userMsg, List<ChatContent> chatContents, String modelType) {
    JSONObject jsonObject = new JSONObject();

    JSONArray jsonMessagesArray = new JSONArray();
    JSONObject firstTimeSetting = new JSONObject();
    firstTimeSetting.put("role", "system");
    firstTimeSetting.put("content", "You are a helpful assistant");
    jsonMessagesArray.add(firstTimeSetting);
    for (int i = 0; i <= chatContents.size() - 1; i++) {
      JSONObject subObject = new JSONObject();
      String serveType =
          chatContents.get(i).getUserType().equals("user")
              ? "user"
              : chatContents.get(i).getUserType().equals("system") ? "system" : "assistant";

      subObject.put("role", serveType);
      subObject.put("content", chatContents.get(i).getContent());
      jsonMessagesArray.add(subObject);
    }

    jsonObject.put("messages", jsonMessagesArray);
    jsonObject.put("model", modelType);
    jsonObject.put("frequency_penalty", 0);
    jsonObject.put("max_tokens", 2048);
    jsonObject.put("presence_penalty", 0);
    jsonObject.put("stop", null);
    jsonObject.put("stream", false);
    jsonObject.put("stream_options", null);
    jsonObject.put("temperature", 1);
    jsonObject.put("top_p", 1);
    jsonObject.put("tools", null);
    jsonObject.put("tool_choice", "none");
    jsonObject.put("logprobs", false);
    jsonObject.put("top_logprobs", null);

    JSONObject jsonFormat = new JSONObject();
    jsonFormat.put("type", "text");
    jsonObject.put("response_format", jsonFormat);
    return jsonObject.toJSONString();
  }
}
