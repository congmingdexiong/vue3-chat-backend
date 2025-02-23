package com.zhuzhule.chatPigZhuzhuleBackend.service;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.ChatContent;
import java.util.List;

/**
 * @author allenliu
 * @description 针对表【chat_content】的数据库操作Service
 * @createDate 2025-02-23 09:58:21
 */
public interface ChatContentService {
  public List<ChatContent> findAll();

  public List<ChatContent> getChatContentByUserId(String userId);

  public List<ChatContent> getChatContentByConversationId(String conversationId);

  public Integer updateContentById(ChatContent chatContent);

  public Integer addChatContent(ChatContent chatContent);
}
