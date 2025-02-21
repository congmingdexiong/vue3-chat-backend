package com.zhuzhule.chatPigZhuzhuleBackend.interceptor;

import com.alibaba.fastjson.JSON;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.Conversation;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.WxResource;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek.RequestPayload;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ConversationService;
import com.zhuzhule.chatPigZhuzhuleBackend.utils.HttpHelper;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ConversationInterceptor implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(ConversationInterceptor.class);

  @Autowired private ConversationService conversationService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    HttpSession session = request.getSession(false);
    //    if (session != null && session.getAttribute("activeConversation") != null) {
    //      logger.info("activeConversation存在，不需要创建");
    //      return true;
    //    }
    WxResource wxRes = (WxResource) session.getAttribute("userStorage");
    String body = HttpHelper.getBodyString(request);
    String responseJsonString = JSON.parseObject(body).toJSONString();
    RequestPayload requestPayload = JSON.parseObject(responseJsonString, RequestPayload.class);
    System.err.println(requestPayload);
    Integer res;
    try {
      if (requestPayload.getOpts().getConversationId() != null) {

        List<Conversation> conversations =
            conversationService.getConversationById(requestPayload.getOpts().getConversationId());

        if (conversations.size() == 0) {
          // 不存在conversation, new
          Conversation conversation = new Conversation();
          LocalDateTime dateTime = LocalDateTime.now();
          conversation.setId(requestPayload.getOpts().getConversationId());
          conversation.setUserId(wxRes.getOpenid());
          conversation.setLabel("New chat");
          conversation.setCreatedTime(dateTime.toString());
          res = conversationService.addConversation(conversation);
          // todo add success or not
          session.setAttribute("activeConversation", conversation);
          logger.info("activeConversation创建成功！！");
        } else {
          session.setAttribute("activeConversation", conversations.get(0));
        }
      }

    } catch (Error e) {
      logger.error("create conversation error {}", e);
    }

    //    response.sendRedirect("/user/login");
    // response.setStatus(401);//向前端返回相应的状态码  401：没有权限
    return true;
  }
}
