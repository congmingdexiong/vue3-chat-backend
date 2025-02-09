package com.zhuzhule.chatPigZhuzhuleBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Configuration
public class ChatPigZhuzhuleBackendApplication {

  public static void main(String[] args) {

    SpringApplication.run(ChatPigZhuzhuleBackendApplication.class, args);
  }

  @RequestMapping(value = "/others")
  public String handleOtherPath() {
    return "redirect";
  }
}
