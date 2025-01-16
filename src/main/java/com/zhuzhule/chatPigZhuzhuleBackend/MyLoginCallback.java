package com.zhuzhule.chatPigZhuzhuleBackend;

import com.tofries.wxlogin.callback.WeixinLoginCallback;
import org.springframework.stereotype.Component;

@Component
public class MyLoginCallback implements WeixinLoginCallback {
    private final UserService userService;

    public MyLoginCallback(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String onLoginSuccess(String sceneId, String openid) {
        System.out.println(sceneId);
        System.out.println(openid);
        return "您好！欢迎光临猪猪乐的空间，返回查看页面";
    }
}