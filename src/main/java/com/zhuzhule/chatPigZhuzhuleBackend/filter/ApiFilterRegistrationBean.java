package com.zhuzhule.chatPigZhuzhuleBackend.filter;

import com.zhuzhule.chatPigZhuzhuleBackend.controller.UserController;
import org.apache.commons.net.util.SubnetUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ApiFilterRegistrationBean extends FilterRegistrationBean<HttpFilter> {
    private static final Logger logger = LoggerFactory.getLogger(ApiFilterRegistrationBean.class);

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
        setOrder(20);  // 设置ApiFilter的执行顺序
        setFilter(new ApiFilter());

//        setUrlPatterns(List.of("/api/*"));  // 只匹配/api/*路径
    }
    class ApiFilter extends HttpFilter {
        @Override
        public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            String ipAddress = getIpAddr(request);
            String cidr = "125.228.0.0/15"; // CIDR表示法

            SubnetUtils subnetUtils = new SubnetUtils(cidr);
            SubnetUtils.SubnetInfo subnetInfo = subnetUtils.getInfo();

            boolean isInRange = subnetInfo.isInRange(ipAddress);
            if (isInRange || isIntranetIP(ipAddress)){
                logger.info(String.format("当前后端请求的路径是%s",request.getRequestURI()));
                chain.doFilter(request, response);
            } else {
                logger.info("ip address不合规,ip address 是{} ",ipAddress);
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Forbidden");
            }


        }
    }


    /**
     * 获取客户端IP地址
     * 由于客户端的IP地址可能通过多个代理层转发，因此需要检查多个HTTP头字段以获取真实IP。
     * 此方法首先检查“x-forwarded-for”头，这是最常用的代理头，然后尝试其他不那么常见的头字段。
     * 如果所有尝试都失败，则回退到使用请求的远程地址。
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


