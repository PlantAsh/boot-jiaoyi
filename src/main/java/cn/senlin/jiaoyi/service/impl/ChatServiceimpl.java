package cn.senlin.jiaoyi.service.impl;

import cn.senlin.jiaoyi.entity.Message;
import cn.senlin.jiaoyi.mapper.MessageMapper;
import cn.senlin.jiaoyi.service.ChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("chatService")
public class ChatServiceimpl implements ChatService {
	
	@Resource
	private MessageMapper messageMapper;

	public List<Message> getMessage(String sendUser, String acceptUser) {
		
		List<Message> message;
		message = messageMapper.getMessage(sendUser, acceptUser);
		if(message != null) {
			messageMapper.updateState(sendUser, acceptUser);
		}
		return message;
	}

	public List<Message> getUser(String acceptUser) {
		
		List<Message> message;
		message = messageMapper.getUser(acceptUser);
		return message;
	}

	public String addMessage(Message message) {
		
		int i = messageMapper.addMessage(message);
		if(i == 0) {
			return "服务器错误";
		} else {
			return "success";
		}
	}

	public int getCount(String acceptUser) {
		String messageState = "未读";
		return messageMapper.getCount(acceptUser, messageState);
	}

}
