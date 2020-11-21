package com.acong.ssoserver.dao;

import org.apache.ibatis.annotations.Mapper;

import com.acong.ssoserver.entity.User;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface UserDao {

	void save(User user);

	HashMap<String,Object> findByUsername(final String name);
}