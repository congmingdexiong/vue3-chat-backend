package com.zhuzhule.chatPigZhuzhuleBackend.domain;

public class Test1_User {
  @Override
  public String toString() {
    return "User{"
        + "id='"
        + id
        + '\''
        + ", nickName='"
        + nickName
        + '\''
        + ", createdTime='"
        + createdTime
        + '\''
        + ", status='"
        + status
        + '\''
        + ", prepare3='"
        + prepare3
        + '\''
        + ", prepare2='"
        + prepare2
        + '\''
        + ", prepare1='"
        + prepare1
        + '\''
        + '}';
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(String createdTime) {
    this.createdTime = createdTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPrepare3() {
    return prepare3;
  }

  public void setPrepare3(String prepare3) {
    this.prepare3 = prepare3;
  }

  public String getPrepare2() {
    return prepare2;
  }

  public void setPrepare2(String prepare2) {
    this.prepare2 = prepare2;
  }

  public String getPrepare1() {
    return prepare1;
  }

  public void setPrepare1(String prepare1) {
    this.prepare1 = prepare1;
  }

  String id;
  String nickName;
  String createdTime;
  String status;
  String prepare3;
  String prepare2;
  String prepare1;
}
