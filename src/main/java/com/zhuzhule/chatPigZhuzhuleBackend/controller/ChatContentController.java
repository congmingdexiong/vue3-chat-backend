package com.zhuzhule.chatPigZhuzhuleBackend.controller;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.ChatContent;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ChatContentService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatContentController {
  @Autowired private ChatContentService chatContentService;

  @GetMapping(value = "/api/getChatMessage")
  @ResponseBody
  public List<ChatContent> getUserInfo(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam String conversationId) {
    List<ChatContent> chatContents =
        chatContentService.getChatContentByConversationId(conversationId);
    return chatContents;
  }
}
