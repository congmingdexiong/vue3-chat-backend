package com.zhuzhule.chatPigZhuzhuleBackend.service;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.User;
import java.util.List;

/**
 * @author allenliu
 * @description 针对表【user】的数据库操作Service
 * @createDate 2025-02-18 15:46:25
 */
public interface UserService {
  public List<User> findAll();

  public Integer addUser(User user);

  public List<User> getUserById(User user);
}
