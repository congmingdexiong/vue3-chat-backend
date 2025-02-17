package com.zhuzhule.chatPigZhuzhuleBackend.filter;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.WxResource;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthFilterRegistrationBean extends FilterRegistrationBean<HttpFilter> {
  private static final Logger logger = LoggerFactory.getLogger(AuthFilterRegistrationBean.class);

  //    @Autowired
  //    private UserService userService;

  @Override
  public HttpFilter getFilter() {

    setOrder(10); // 设置Filter执行顺序，值小的先执行
    return new AuthFilter();
  }

  class AuthFilter extends HttpFilter {
    @Override
    public void doFilter(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      logger.info("当前请求路径是:{}", request.getRequestURI());
      HttpSession session = request.getSession();
      WxResource wxRes = (WxResource) session.getAttribute("userStorage");
      if (StringUtils.isEmpty(wxRes)) {
        if (request.getRequestURI().equals("/wechat/callback1")) {
          logger.info("当前用户没有信息存储");
          response.setContentType("text/plain");
          response.setCharacterEncoding("UTF-8");
          response.getWriter().write("Not authorized");
        } else if (request.getRequestURI().startsWith("/api")) {
          logger.info("当前用户没有信息存储");
          response.setContentType("text/plain");
          response.setCharacterEncoding("UTF-8");
          response.getWriter().write("Not authorized");
        } else {
          chain.doFilter(request, response);
        }
      } else {
        chain.doFilter(request, response);
      }
    }
  }
}
