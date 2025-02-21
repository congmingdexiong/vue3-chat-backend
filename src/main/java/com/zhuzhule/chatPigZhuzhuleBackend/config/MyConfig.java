package com.zhuzhule.chatPigZhuzhuleBackend.config;

import com.zhuzhule.chatPigZhuzhuleBackend.interceptor.ConversationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {
  @Autowired private ConversationInterceptor conversationInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(conversationInterceptor).addPathPatterns("/api/deepseek", "/api/baidu");
  }
}
