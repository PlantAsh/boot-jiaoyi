package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.User;

public interface UserService {
	String addUser(User user);
	
	User loadUser(String userAccount);
	
	String updatePassword(User user);

}
