package com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek;

public class RequestPayload {
  @Override
  public String toString() {
    return "RequestPayload{" + "userMsg='" + userMsg + '\'' + ", opts=" + opts.toString() + '}';
  }

  public String getUserMsg() {
    return userMsg;
  }

  public void setUserMsg(String userMsg) {
    this.userMsg = userMsg;
  }

  public Opts getOpts() {
    return opts;
  }

  public void setOpts(Opts opts) {
    this.opts = opts;
  }

  String userMsg;

  Opts opts;
}
