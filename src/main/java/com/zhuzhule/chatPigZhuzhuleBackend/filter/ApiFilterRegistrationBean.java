package com.zhuzhule.chatPigZhuzhuleBackend.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

// @Component
public class ApiFilterRegistrationBean extends FilterRegistrationBean<HttpFilter> {
  private static final Logger logger = LoggerFactory.getLogger(ApiFilterRegistrationBean.class);

  static String UNKNOWN = "unknown";

  private static final String INTRANET_IP_REGEX =
      "10\\.(\\d{1,3}\\.){2}\\d{1,3}$|172\\.1[6-9]\\.(\\d{1,3}\\.)\\d{1,3}$|172\\.2[0-9]\\.(\\d{1,3}\\.)\\d{1,3}$|172\\.3[0-1]\\.(\\d{1,3}\\.)\\d{1,3}$|192\\.168\\.(\\d{1,3}\\.)\\d{1,3}$";

  // 编译正则表达式
  private static final Pattern INTRANET_IP_PATTERN = Pattern.compile(INTRANET_IP_REGEX);

  // 方法：检查IP地址是否为内网IP
  public static boolean isIntranetIP(String ipAddress) {
    if (ipAddress == null || ipAddress.isEmpty()) {
      return false;
    }

    Matcher matcher = INTRANET_IP_PATTERN.matcher(ipAddress);
    return matcher.matches();
  }

  @PostConstruct
  public void init() {
    setOrder(9); // 设置ApiFilter的执行顺序
    setFilter(new ApiFilter());

    //        setUrlPatterns(List.of("/api/*"));  // 只匹配/api/*路径
  }

  /**
   * 获取IP公网地址 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
   * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
   */
  public static String getIPAddress(HttpServletRequest request) {
    String ip = null;
    try {
      // 以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
      ip = request.getHeader("X-Original-Forwarded-For");
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        // X-Forwarded-For：Squid 服务代理
        ip = request.getHeader("X-Forwarded-For");
      }
      // 获取nginx等代理的ip
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getHeader("x-forwarded-for");
      }
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        // Proxy-Client-IP：apache 服务代理
        ip = request.getHeader("Proxy-Client-IP");
      }
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        // WL-Proxy-Client-IP：weblogic 服务代理
        ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        // HTTP_CLIENT_IP：有些代理服务器
        ip = request.getHeader("HTTP_CLIENT_IP");
      }
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
      }
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        // X-Real-IP：nginx服务代理
        ip = request.getHeader("X-Real-IP");
      }
      // 兼容k8s集群获取ip
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
        if ("127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
          // 根据网卡取本机配置的IP
          InetAddress iNet = null;
          try {
            iNet = InetAddress.getLocalHost();
          } catch (UnknownHostException e) {
            logger.error("getClientIp error: {}", e);
          }
          ip = iNet.getHostAddress();
        }
      }
      // 使用代理，则获取第一个IP地址
      if (StringUtils.isNotBlank(ip) && ip.indexOf(",") > 0) {
        ip = ip.substring(0, ip.indexOf(","));
      }
      // 还是不能获取到，最后再通过request.getRemoteAddr();获取
      if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
      }
    } catch (Exception e) {
      logger.error("getClientIp error ", e);
    }
    return ip;
  }

  class ApiFilter extends HttpFilter {
    @Override
    public void doFilter(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      String ipAddress = getIPAddress(request);
      String cidr = "125.228.0.0/15"; // CIDR表示法

      SubnetUtils subnetUtils = new SubnetUtils(cidr);
      SubnetUtils.SubnetInfo subnetInfo = subnetUtils.getInfo();

      boolean isInRange = subnetInfo.isInRange(ipAddress);
      logger.info("ip address 是{} ", ipAddress);
      if (isInRange || isIntranetIP(ipAddress)) {
        logger.info(String.format("当前后端请求的路径是%s", request.getRequestURI()));
        chain.doFilter(request, response);
      } else {
        logger.info("ip address不合规,ip address 是{} ", ipAddress);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("Forbidden");
      }
    }
  }

  /**
   * 获取客户端IP地址 由于客户端的IP地址可能通过多个代理层转发，因此需要检查多个HTTP头字段以获取真实IP。
   * 此方法首先检查“x-forwarded-for”头，这是最常用的代理头，然后尝试其他不那么常见的头字段。 如果所有尝试都失败，则回退到使用请求的远程地址。
   *
   * @param request HttpServletRequest对象，用于获取客户端IP地址。
   * @return 客户端的IP地址字符串。如果无法确定客户端IP，则返回请求的远程地址。
   */
  protected String getIpAddr(HttpServletRequest request) {
    // 尝试获取“x-forwarded-for”头，这是最常用的代理头字段。
    String ip = request.getHeader("x-forwarded-for");
    // 检查“x-forwarded-for”头是否有效，并提取第一个IP地址。
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个ip值，第一个ip才是真实ip
      if (ip.indexOf(",") != -1) {
        ip = ip.split(",")[0];
      }
    }
    // 如果“x-forwarded-for”头无效，尝试其他不那么常见的代理头字段。
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP");
    }
    // 如果所有代理头字段都无效，回退到使用请求的远程地址作为客户端IP。
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    // 返回获取到的IP地址，无论它是通过代理头还是直接从请求中获取。
    return ip;
  }
}
