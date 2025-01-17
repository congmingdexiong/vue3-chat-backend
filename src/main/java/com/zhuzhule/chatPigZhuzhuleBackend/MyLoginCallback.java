package com.zhuzhule.chatPigZhuzhuleBackend;

import com.tofries.wxlogin.callback.WeixinLoginCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyLoginCallback implements WeixinLoginCallback {
    private static final Logger logger = LoggerFactory.getLogger(MyLoginCallback.class);

    private final UserService userService;

    public MyLoginCallback(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String onLoginSuccess(String sceneId, String openid) {
        logger.info("用户微信公众号回调成功sceneId : {}, openId : {}, code : {}",sceneId,openid);
        return "您好！欢迎光临猪猪乐的空间，返回查看页面";
    }
}