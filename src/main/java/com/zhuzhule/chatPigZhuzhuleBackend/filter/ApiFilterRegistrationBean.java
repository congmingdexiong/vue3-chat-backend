package com.zhuzhule.chatPigZhuzhuleBackend.filter;

import com.zhuzhule.chatPigZhuzhuleBackend.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiFilterRegistrationBean extends FilterRegistrationBean<HttpFilter> {
    private static final Logger logger = LoggerFactory.getLogger(ApiFilterRegistrationBean.class);
    @PostConstruct
    public void init() {
        setOrder(20);  // 设置ApiFilter的执行顺序
        setFilter(new ApiFilter());

//        setUrlPatterns(List.of("/api/*"));  // 只匹配/api/*路径
    }
    class ApiFilter extends HttpFilter {
        @Override
        public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            logger.info(String.format("当前后端请求的路径是%s",request.getRequestURI()));
            chain.doFilter(request, response);
        }
    }
}