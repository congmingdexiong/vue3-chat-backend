package com.zhuzhule.chatPigZhuzhuleBackend.mapper;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.User;
import java.util.List;

/**
 * @author allenliu
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2025-02-18 15:46:25 @Entity com.zhuzhule.chatPigZhuzhuleBackend.domain.User
 */
public interface UserMapper {
  public List<User> findAll();

  public Integer addUser(User user);

  public List<User> getUserById(User user);
}
