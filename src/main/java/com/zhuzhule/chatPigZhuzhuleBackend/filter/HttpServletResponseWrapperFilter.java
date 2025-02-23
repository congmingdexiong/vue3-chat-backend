package com.zhuzhule.chatPigZhuzhuleBackend.filter;

import com.zhuzhule.chatPigZhuzhuleBackend.config.HttpServletResponseCopier;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

/** Get response body */
public class HttpServletResponseWrapperFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (response.getCharacterEncoding() == null) {
      response.setCharacterEncoding("UTF-8");
    }
    HttpServletResponseCopier copier =
        new HttpServletResponseCopier((HttpServletResponse) response);
    chain.doFilter(request, copier);
    copier.flushBuffer();
  }

  @Override
  public void destroy() {}
}
