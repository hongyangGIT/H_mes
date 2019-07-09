package com.hongyang.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hongyang.dao.UserMapper;
import com.hongyang.model.User;

@Service
public class UserService {
	@Resource
	private UserMapper userMapper;

	public void addUser(User user) {
		userMapper.insert(user);
	}
	
}
