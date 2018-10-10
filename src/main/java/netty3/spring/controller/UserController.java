package netty3.spring.controller;

import org.springframework.stereotype.Controller;

import netty3.spring.annotation.Action;

@Controller
public class UserController {

	@Action("saveUser")
	public void saveUser(String username,String password) {
		System.out.println("username : " + username + " , password : " + password);
	}
	
	@Action("getUser")
	public String getUser() {
		return "zhangsan";
	}
}
