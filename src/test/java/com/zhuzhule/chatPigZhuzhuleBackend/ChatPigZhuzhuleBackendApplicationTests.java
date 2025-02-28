package com.zhuzhule.chatPigZhuzhuleBackend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.ChatContent;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ChatContentService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatPigZhuzhuleBackendApplicationTests {
  @Autowired private ChatContentService chatContentService;

  @Test
  void contextLoads() {
    List<ChatContent> chatContents =
        chatContentService.getChatContentByConversationId("5e054cec-4b2a-43d7-af5d-9facace09a57");

    JSONObject jsonObject = new JSONObject();

    //    JSONArray jsonSubArray = new JSONArray();
    //    JSONObject subObject1 = new JSONObject();
    //    JSONObject subObject2 = new JSONObject();
    //    subObject1.put("role", "user");
    //    subObject1.put("content", "What's the highest mountain in the world?");
    //    subObject2.put("role", "assistant");
    //    subObject2.put("content", "The highest mountain in the world is Mount Everest.");
    //    jsonSubArray.add(subObject1);
    //    jsonSubArray.add(subObject2);
    //    jsonObject.put("messages", jsonSubArray);
    //
    //    System.err.println(jsonObject.toJSONString());
    //
    //    System.err.println(chatContents);
    //    String inputStr = this.getJSONString("hi, who am i?", chatContents, "deepseek-chat");
    //    System.err.println(chatContents);
    System.err.println(chatContents.get(1).getUserType());
    System.err.println(chatContents.get(1).getUserType() == "user");
    String serveType =
        chatContents.get(1).getUserType().equals("user")
            ? "user"
            : chatContents.get(1).getUserType().equals("system") ? "system" : "assistant";
    System.err.println(serveType);
  }

  public String getJSONString(String userMsg, List<ChatContent> chatContents, String modelType) {
    JSONObject jsonObject = new JSONObject();

    JSONArray jsonMessagesArray = new JSONArray();
    for (int i = 0; i < chatContents.size() - 1; i++) {
      JSONObject subObject = new JSONObject();
      String serveType = chatContents.get(i).getUserType() == "user" ? "user" : "assistant";
      subObject.put("role", serveType);
      subObject.put("content", chatContents.get(i).getContent());
      jsonMessagesArray.add(subObject);
    }
    JSONObject userCurrentInput = new JSONObject();
    userCurrentInput.put("role", "user");
    userCurrentInput.put("content", userMsg);

    jsonMessagesArray.add(userCurrentInput);

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
