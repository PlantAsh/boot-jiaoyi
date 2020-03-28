package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.InformationCode;
import cn.senlin.jiaoyi.entity.UserInformation;

import java.util.List;

public interface InformationService {
	UserInformation loadInformation(String userAccount) throws Exception;
	
	List<InformationCode> loadByType(String codeType) throws Exception;
	
	String updateInformation(UserInformation usin, String userName) throws Exception;
}
