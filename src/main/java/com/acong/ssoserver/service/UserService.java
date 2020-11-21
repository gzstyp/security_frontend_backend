package com.acong.ssoserver.service;

import org.springframework.stereotype.Service;

import com.acong.ssoserver.dao.UserDao;
import com.acong.ssoserver.entity.User;

import javax.annotation.Resource;

@Service
public class UserService {
	
	@Resource
	private UserDao userDao;

	public void save(User user) {
		user.setId(1);
		userDao.save(user);
	}
}