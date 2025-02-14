package com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek;

public class Opts {
  public Boolean getEnabledReasoner() {
    return enabledReasoner;
  }

  public void setEnabledReasoner(Boolean enabledReasoner) {
    this.enabledReasoner = enabledReasoner;
  }

  @Override
  public String toString() {
    return "Opts{" + "enabledReasoner='" + enabledReasoner + '\'' + '}';
  }

  Boolean enabledReasoner;
}
