package com.zhuzhule.chatPigZhuzhuleBackend.service;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.Conversation;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.User;
import java.util.List;

/**
 * @author allenliu
 * @description 针对表【conversation】的数据库操作Service
 * @createDate 2025-02-19 11:13:12
 */
public interface ConversationService {
  public List<Conversation> findAll();

  public Integer addConversation(Conversation conversation);

  public List<Conversation> getConversationByUserId(User user);

  public Integer deleteConversationById(String id);

  public Integer updateLabelById(Conversation conversation);
}
