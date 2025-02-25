package com.zhuzhule.chatPigZhuzhuleBackend.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.*;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ChatContentService;
import com.zhuzhule.chatPigZhuzhuleBackend.service.ConversationService;
import com.zhuzhule.chatPigZhuzhuleBackend.service.TestUserService;
import com.zhuzhule.chatPigZhuzhuleBackend.service.UserService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserConversationController {

  @Autowired private TestUserService testUserService;

  @Autowired private UserService userService;

  @Autowired private ConversationService conversationService;

  @Autowired private ChatContentService chatContentService;

  private static final Logger logger = LoggerFactory.getLogger(UserConversationController.class);

  static final OkHttpClient HTTP_CLIENT =
      new OkHttpClient()
          .newBuilder()
          .connectTimeout(50, TimeUnit.SECONDS)
          .readTimeout(55, TimeUnit.SECONDS)
          .writeTimeout(55, TimeUnit.MILLISECONDS)
          .build();

  @Value("${wxlogin.app-id}")
  public String WX_APP_ID;

  @Value("${wxlogin.app-secret}")
  public String WX_APP_SECRET;

  @GetMapping(value = "/wechat/callback1")
  public String getTest(Model model) throws IOException {
    return "index";
  }

  @GetMapping(value = "/wechat/userInfo")
  @ResponseBody
  public WxResource getUserInfo(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    WxResource wxRes = (WxResource) session.getAttribute("userStorage");
    if (StringUtils.isEmpty(wxRes) || StringUtils.isEmpty(wxRes.getNickname())) {
      return null;
    }
    User user = new User();
    user.setId(wxRes.getOpenid());
    List<Conversation> conversations = conversationService.getConversationByUserId(user);
    wxRes.setConversations(conversations);
    return wxRes;
  }

  @PostMapping(value = "/api/createConversation")
  @ResponseBody
  public Number createConversation(
      @org.springframework.web.bind.annotation.RequestBody Conversation conversation,
      HttpServletRequest request,
      HttpServletResponse response) {
    Integer res;
    HttpSession session = request.getSession();
    WxResource wxRes = (WxResource) session.getAttribute("userStorage");
    if (StringUtils.isEmpty(wxRes)) {
      logger.info("当前用户没有信息存储");
    }
    try {
      LocalDateTime dateTime = LocalDateTime.now();
      conversation.setId(conversation.getId());
      conversation.setUserId(wxRes.getOpenid());
      conversation.setLabel("New chat");
      conversation.setCreatedTime(dateTime.toString());
      res = conversationService.addConversation(conversation);
    } catch (Error e) {
      logger.error("service error {}", e);
      return -1;
    }
    return res;
  }

  @PostMapping(value = "/api/updateConversation")
  @ResponseBody
  public Number updateConversation(
      @org.springframework.web.bind.annotation.RequestBody Conversation conversation,
      HttpServletRequest request,
      HttpServletResponse response) {
    Integer res;
    try {
      res = conversationService.updateLabelById(conversation);
    } catch (Error e) {
      logger.error("service error {}", e);
      return -1;
    }
    return res;
  }

  @PostMapping(value = "/api/deleteConversation")
  @ResponseBody
  public Number deleteConversation(
      @org.springframework.web.bind.annotation.RequestBody Conversation conversation,
      HttpServletRequest request,
      HttpServletResponse response) {
    Integer res;
    try {
      res = conversationService.deleteConversationById(conversation.getId());
    } catch (Error e) {
      logger.error("service error {}", e);
      return -1;
    }
    return res;
  }

  @PostMapping(value = "/wechat/addUser")
  @ResponseBody
  public WxResource addUserInfo(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    WxResource wxResource = new WxResource();
    wxResource.setOpenid("testK7CxeAw_hRJ1ARiIae7FhiuQ");
    wxResource.setSex(0);
    wxResource.setNickname("祝祝乐Test");
    wxResource.setHeadimgurl(
        "https://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaELUV5cibzXYYvDEV2CQvNhRklNE8f2OtwJiaVPiaGBUfZyZ5YsiaJaFeIJux63Uf9kbH80sjaPcLKL8TQ/132");
    session.setAttribute("userStorage", wxResource);

    this.mapper2dbUser(wxResource);
    logger.info("你设置了一个测试用户,userInfo:{}", wxResource.toString());
    return wxResource;
  }

  @GetMapping(value = "/wechat/callback")
  public String getCallback(String code, HttpServletRequest request, Model model)
      throws IOException {

    logger.info("微信授权登录返回的code : {}", code);
    String requestUrl =
        "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
            + WX_APP_ID
            + "&secret="
            + WX_APP_SECRET
            + "&code="
            + code
            + "&grant_type=authorization_code";
    Request requestToApi =
        new Request.Builder().url(requestUrl).addHeader("Content-Type", "application/json").build();

    Response responseFromApi = HTTP_CLIENT.newCall(requestToApi).execute();
    String resultToken = JSON.parseObject(responseFromApi.body().string()).toJSONString();
    ObjectMapper mapper = new ObjectMapper();
    WxUserToken wxUserToken = mapper.readValue(resultToken, WxUserToken.class);
    String resourceToken = wxUserToken.getAccessToken();
    String openid = wxUserToken.getOpenid();

    String resourceUrl =
        "https://api.weixin.qq.com/sns/userinfo?access_token="
            + resourceToken
            + "&openid="
            + openid
            + "&lang=zh_CN";
    Request requestToResource =
        new Request.Builder()
            .url(resourceUrl)
            .addHeader("Content-Type", "application/json")
            .build();
    Response responseFromResource = HTTP_CLIENT.newCall(requestToResource).execute();
    String resultResource = JSON.parseObject(responseFromResource.body().string()).toJSONString();

    WxResource wxUserResource = mapper.readValue(resultResource, WxResource.class);
    this.mapper2dbUser(wxUserResource);
    HttpSession session = request.getSession();
    session.setAttribute("userStorage", wxUserResource);
    WxResource wxRes = (WxResource) session.getAttribute("userStorage");
    if (StringUtils.isEmpty(wxRes)) {
      logger.info("当前用户没有信息存储");
    }

    return "index";
  }

  private void mapper2dbUser(WxResource wxResource) {
    User user = new User();
    user.setId(wxResource.getOpenid());
    user.setStatus("active");
    user.setNickName(wxResource.getNickname());
    user.setPrepare1("");
    user.setPrepare2("");
    user.setPrepare3("");
    LocalDateTime dateTime = LocalDateTime.now();
    user.setCreatedTime(dateTime.toString());

    List<User> users = userService.getUserById(user);
    if (users.isEmpty()) {
      logger.info("用户添加成功！");
      userService.addUser(user);
    } else {
      logger.info("用户存在查询成功:{}", users);
    }
  }
}
