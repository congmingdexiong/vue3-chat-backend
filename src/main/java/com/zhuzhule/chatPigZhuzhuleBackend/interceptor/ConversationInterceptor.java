package com.zhuzhule.chatPigZhuzhuleBackend.interceptor;

import static com.zhuzhule.chatPigZhuzhuleBackend.utils.IdGenerator.generateUUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuzhule.chatPigZhuzhuleBackend.config.HttpServletResponseCopier;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.ChatContent;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.Conversation;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.WxResource;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek.RequestPayload;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ChatContentService;
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
import org.springframework.web.servlet.ModelAndView;

@Component
public class ConversationInterceptor implements HandlerInterceptor {

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    byte[] copy = ((HttpServletResponseCopier) response).getCopy();
    String str = new String(copy, "UTF-8");
    HttpSession session = request.getSession(false);
    JSONObject responseJson = JSON.parseObject(str);
    ChatContent content = new ChatContent();
    WxResource wxRes = (WxResource) session.getAttribute("userStorage");
    Conversation conversation = (Conversation) session.getAttribute("activeConversation");
    LocalDateTime dateTime = LocalDateTime.now();

    content.setId(generateUUID());
    content.setConversationId(conversation.getId());
    content.setUserType("bot");
    content.setCreatedTime(dateTime.toString());
    content.setContent((String) responseJson.get("result"));
    content.setUserId(wxRes.getOpenid());
    chatContentService.addChatContent(content);
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  private static final Logger logger = LoggerFactory.getLogger(ConversationInterceptor.class);

  @Autowired private ConversationService conversationService;

  @Autowired private ChatContentService chatContentService;

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
    String aiType = requestPayload.getOpts().getAiType();
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
          conversation.setLabel(requestPayload.getUserMsg());
          conversation.setCreatedTime(dateTime.toString());
          if (requestPayload.getOpts().getEnabledReasoner() == null) {
            conversation.setAiType(aiType);
          } else {
            conversation.setAiType(
                requestPayload.getOpts().getEnabledReasoner() ? "deepseek-reasoner" : aiType);
          }
          res = conversationService.addConversation(conversation);
          // todo add success or not
          session.setAttribute("activeConversation", conversation);
          logger.info("activeConversation创建成功,id:{}", conversation.getId());
        } else {
          logger.info("activeConversation 已经存在 id:{}", conversations.get(0).getId());
          session.setAttribute("activeConversation", conversations.get(0));
        }
        Conversation conversationSession =
            (Conversation) session.getAttribute("activeConversation");

        ChatContent content = new ChatContent();
        content.setId(generateUUID());
        content.setConversationId(conversationSession.getId());
        content.setUserId(wxRes.getOpenid());
        content.setContent(requestPayload.getUserMsg());
        content.setUserType("user");
        LocalDateTime dateTime = LocalDateTime.now();
        content.setCreatedTime(dateTime.toString());
        chatContentService.addChatContent(content);
      }

    } catch (Error e) {
      logger.error("create conversation error {}", e);
    }

    //    response.sendRedirect("/user/login");
    // response.setStatus(401);//向前端返回相应的状态码  401：没有权限
    return true;
  }
}
