package com.hongyang.controller;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hongyang.model.User;
import com.hongyang.service.UserService;

@Controller
@RequestMapping("/sys")
public class TestController {
	@Resource
	private UserService userService;
	@RequestMapping("/index.page")
	public String indexPage() {
		return "index";
	}
	@RequestMapping("/test.page")
	public String testPage() {
		return "test";
	} 
	@RequestMapping("/test.json")
	public void testJson(User user) {
		System.out.println(user.getName());
		userService.addUser(user);
	}
	@RequestMapping("/admin.page")
	public String adminPage() {
		return "admin";
	}
	@RequestMapping("dept.page")
	public String deptPage() {
		return "dept";
	}
	
}
