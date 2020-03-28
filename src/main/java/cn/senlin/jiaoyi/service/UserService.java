package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.User;

public interface UserService {
	public String addUser(User user) throws Exception;
	
	public User loadUser(String userAccount) throws Exception;
	
	public String updatePassword(User user) throws Exception;

}
