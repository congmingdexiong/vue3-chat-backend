package com.zhuzhule.chatPigZhuzhuleBackend.service.impl;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.TestUser;
import com.zhuzhule.chatPigZhuzhuleBackend.mapper.TestUserMapper;
import com.zhuzhule.chatPigZhuzhuleBackend.service.TestUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestUserServiceImpl implements TestUserService {
  @Autowired() private TestUserMapper testUserMapper;

  @Override
  public List<TestUser> findAll() {
    return testUserMapper.findAll();
  }
}
