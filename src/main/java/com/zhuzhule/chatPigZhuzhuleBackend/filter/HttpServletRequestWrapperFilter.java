package com.zhuzhule.chatPigZhuzhuleBackend.filter;

import com.zhuzhule.chatPigZhuzhuleBackend.config.BodyReaderHttpServletRequestWrapper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:lanxing@chances.com.cn">lanxing</a>
 */
@Component
@WebFilter(
    filterName = "httpServletRequestWrapperFilter",
    urlPatterns = {"/*"})
public class HttpServletRequestWrapperFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    ServletRequest requestWrapper = null;
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;

      // 遇到post方法才对request进行包装
      String methodType = httpRequest.getMethod();
      // 上传文件时同样不进行包装
      String servletPath = httpRequest.getRequestURI().toString();
      if ("POST".equals(methodType)
          && (servletPath.contains("/api/deepseek") || servletPath.contains("/api/baidu"))) {
        requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
      }
    }

    if (null == requestWrapper) {
      chain.doFilter(request, response);
    } else {
      chain.doFilter(requestWrapper, response);
    }
  }

  @Override
  public void destroy() {}
}
