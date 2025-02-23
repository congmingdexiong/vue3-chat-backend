package com.zhuzhule.chatPigZhuzhuleBackend.config;

import com.zhuzhule.chatPigZhuzhuleBackend.filter.HttpServletRequestWrapperFilter;
import com.zhuzhule.chatPigZhuzhuleBackend.filter.HttpServletResponseWrapperFilter;
import com.zhuzhule.chatPigZhuzhuleBackend.interceptor.ConversationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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

  @Bean
  public FilterRegistrationBean<HttpServletResponseWrapperFilter> responseWrapperFilter() {
    FilterRegistrationBean<HttpServletResponseWrapperFilter> registrationBean =
        new FilterRegistrationBean<>();
    registrationBean.setFilter(new HttpServletResponseWrapperFilter());
    registrationBean.addUrlPatterns("/api/deepseek", "/api/baidu"); // 适用于指定的URL路径
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<HttpServletRequestWrapperFilter> requestWrapperFilter() {
    FilterRegistrationBean<HttpServletRequestWrapperFilter> registrationBean =
        new FilterRegistrationBean<>();
    registrationBean.setFilter(new HttpServletRequestWrapperFilter());
    registrationBean.addUrlPatterns("/api/deepseek", "/api/baidu"); // 适用于指定的URL路径
    return registrationBean;
  }
}
