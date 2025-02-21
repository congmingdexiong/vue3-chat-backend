package com.zhuzhule.chatPigZhuzhuleBackend.service.impl;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.Conversation;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.User;
import com.zhuzhule.chatPigZhuzhuleBackend.mapper.ConversationMapper;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ConversationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author allenliu
 * @description 针对表【conversation】的数据库操作Service实现
 * @createDate 2025-02-19 11:13:12
 */
@Service
public class ConversationServiceImpl implements ConversationService {
  @Autowired private ConversationMapper conversationMapper;

  @Override
  public List<Conversation> findAll() {
    return conversationMapper.findAll();
  }

  @Override
  public Integer addConversation(Conversation conversation) {
    return conversationMapper.addConversation(conversation);
  }

  @Override
  public List<Conversation> getConversationByUserId(User user) {
    return conversationMapper.getConversationByUserId(user);
  }

  @Override
  public List<Conversation> getConversationById(String conversationId) {
    return conversationMapper.getConversationById(conversationId);
  }

  @Override
  public Integer deleteConversationById(String id) {
    return conversationMapper.deleteConversationById(id);
  }

  @Override
  public Integer updateLabelById(Conversation conversation) {
    return conversationMapper.updateLabelById(conversation);
  }
}
