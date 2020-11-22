package com.fwtai.service;

import com.fwtai.dao.UserDao;
import com.fwtai.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
	
	@Resource
	private UserDao userDao;

	public void save(final User user) {
		userDao.save(user);
	}
}