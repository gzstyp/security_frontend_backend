package com.fwtai.dao;

import com.fwtai.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface UserDao {

	void save(User user);

	HashMap<String,Object> findByUsername(final String name);
}