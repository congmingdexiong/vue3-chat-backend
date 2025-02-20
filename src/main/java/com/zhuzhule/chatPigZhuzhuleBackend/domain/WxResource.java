package com.zhuzhule.chatPigZhuzhuleBackend.domain;

import java.util.List;
import lombok.Data;

@Data
public class WxResource {

  String country;

  String province;

  String city;

  String openid;

  Number sex;

  String nickname;

  String headimgurl;

  String language;

  String[] privilege;

  List<Conversation> conversations;
}
