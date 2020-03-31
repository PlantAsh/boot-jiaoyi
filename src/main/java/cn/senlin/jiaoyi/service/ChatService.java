package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.Message;

import java.util.List;

public interface ChatService {
	List<Message> getMessage(String sendUser, String acceptUser);

	List<Message> getUser(String acceptUser);

	String addMessage(Message message);
	
	int getCount(String acceptUser);

}
