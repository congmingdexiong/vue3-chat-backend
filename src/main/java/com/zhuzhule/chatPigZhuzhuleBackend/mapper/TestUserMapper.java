package com.zhuzhule.chatPigZhuzhuleBackend.mapper;

import com.zhuzhule.chatPigZhuzhuleBackend.domain.TestUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestUserMapper {
  @Select("SELECT * FROM TEST_USER")
  List<TestUser> findAll();
}
