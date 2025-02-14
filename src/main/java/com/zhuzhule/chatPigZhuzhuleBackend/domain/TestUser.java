package com.zhuzhule.chatPigZhuzhuleBackend.domain;

public class TestUser {
  @Override
  public String toString() {
    return "TestUser{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private Integer id;
  private String name;
}
