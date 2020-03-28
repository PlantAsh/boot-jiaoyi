package cn.senlin.jiaoyi.mapper;

import cn.senlin.jiaoyi.entity.Message;

import java.util.List;

public interface MessageMapper {
	List<Message> getMessage(String messageSend, String messageAccept);
	
	List<Message> getUser(String messageAccept);
	
	int addMessage(Message message);
    
    int getCount(String messageAccept, String messageState);
    
    void updateState(String messageSend, String messageAccept);
}