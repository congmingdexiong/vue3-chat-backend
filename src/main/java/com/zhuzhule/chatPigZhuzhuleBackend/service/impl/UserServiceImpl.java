package com.zhuzhule.chatPigZhuzhuleBackend.service.impl;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.User;
import com.zhuzhule.chatPigZhuzhuleBackend.mapper.UserMapper;
import com.zhuzhule.chatPigZhuzhuleBackend.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author allenliu
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-02-18 15:46:25
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired() private UserMapper userMapper;

  @Override
  public List<User> findAll() {
    return userMapper.findAll();
  }

  @Override
  public Integer addUser(User user) {
    return userMapper.addUser(user);
  }

  @Override
  public List<User> getUserById(User user) {
    return userMapper.getUserById(user);
  }
}
