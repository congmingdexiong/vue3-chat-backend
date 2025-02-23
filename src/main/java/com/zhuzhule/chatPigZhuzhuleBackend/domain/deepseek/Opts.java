package com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek;

import lombok.Data;

@Data
public class Opts {
  public Boolean getEnabledReasoner() {
    return enabledReasoner;
  }

  public void setEnabledReasoner(Boolean enabledReasoner) {
    this.enabledReasoner = enabledReasoner;
  }

  Boolean enabledReasoner;

  String conversationId;

  String aiType;
}
