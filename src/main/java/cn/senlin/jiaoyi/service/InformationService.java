package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.InformationCode;
import cn.senlin.jiaoyi.entity.UserInformation;

import java.util.List;

public interface InformationService {
	UserInformation loadInformation(String userAccount);
	
	List<InformationCode> loadByType(String codeType);
	
	String updateInformation(UserInformation usin, String userName);
}
