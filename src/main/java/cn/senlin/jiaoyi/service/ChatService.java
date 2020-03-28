package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.Message;

import java.util.List;

public interface ChatService {
	public List<Message> getMessage(String sendUser, String acceptUser) throws Exception;

	public List<Message> getUser(String acceptUser) throws Exception;

	public String addMessage(Message message) throws Exception;
	
	public int getCount(String acceptUser) throws Exception;

}
