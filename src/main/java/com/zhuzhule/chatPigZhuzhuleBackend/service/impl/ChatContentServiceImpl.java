package com.zhuzhule.chatPigZhuzhuleBackend.service.impl;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.ChatContent;
import com.zhuzhule.chatPigZhuzhuleBackend.mapper.ChatContentMapper;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ChatContentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author allenliu
 * @description 针对表【chat_content】的数据库操作Service实现
 * @createDate 2025-02-23 09:58:21
 */
@Service
public class ChatContentServiceImpl implements ChatContentService {

  @Autowired private ChatContentMapper chatContentMapper;

  @Override
  public List<ChatContent> findAll() {
    return chatContentMapper.findAll();
  }

  @Override
  public List<ChatContent> getChatContentByUserId(String userId) {
    return chatContentMapper.getChatContentByUserId(userId);
  }

  @Override
  public List<ChatContent> getChatContentByConversationId(String conversationId) {

    return chatContentMapper.getChatContentByConversationId(conversationId);
  }

  @Override
  public Integer updateContentById(ChatContent chatContent) {
    return chatContentMapper.updateContentById(chatContent);
  }

  @Override
  public Integer addChatContent(ChatContent chatContent) {
    return chatContentMapper.addChatContent(chatContent);
  }
}
