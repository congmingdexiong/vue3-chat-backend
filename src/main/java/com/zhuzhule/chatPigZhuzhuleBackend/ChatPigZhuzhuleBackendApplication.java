package com.zhuzhule.chatPigZhuzhuleBackend;

import java.io.FileNotFoundException;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@MapperScan(basePackages = "com.zhuzhule.chatPigZhuzhuleBackend.mapper")
public class ChatPigZhuzhuleBackendApplication {
  private static final Logger logger =
      LoggerFactory.getLogger(ChatPigZhuzhuleBackendApplication.class);

  public static void main(String[] args) throws FileNotFoundException {
    String classpath = ResourceUtils.getURL("classpath:").getPath();
    logger.info("当前classpath是: {}", classpath);
    SpringApplication.run(ChatPigZhuzhuleBackendApplication.class, args);
  }

  @RequestMapping(value = "/others")
  public String handleOtherPath() {
    return "redirect";
  }
}
