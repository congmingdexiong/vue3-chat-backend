package com.zhuzhule.chatPigZhuzhuleBackend.domain;

import lombok.Data;

@Data
public class Result {
  String id;
  String result;
  Conversation conversation;
}
