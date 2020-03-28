package cn.senlin.jiaoyi.service.impl;

import cn.senlin.jiaoyi.entity.User;
import cn.senlin.jiaoyi.entity.UserInformation;
import cn.senlin.jiaoyi.mapper.UserMapper;
import cn.senlin.jiaoyi.mapper.UserInformationMapper;
import cn.senlin.jiaoyi.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceimpl implements UserService {
	@Resource
	private UserMapper userMapper;
	
	@Resource
	private UserInformationMapper userInformationMapper;

	public String addUser(User user) {
		
		User us;
		us = userMapper.loadUser(user.getUserAccount());
		if (us == null) {
			userMapper.addUser(user);
			UserInformation usin = new UserInformation();
			usin.setUserAccount(user.getUserAccount());
			usin.setUserName(user.getUserAccount());
			userInformationMapper.addSelective(usin);
			return "success";
		} else {
			return "账号已存在";
		}
	}

	public User loadUser(String userAccount) {
		return userMapper.loadUser(userAccount);
	}

	public String updatePassword(User user) {
		
		int i = userMapper.updatePassword(user);
		if(i == 0) {
			return "服务器错误";
		}
		return "success";
	}

}
