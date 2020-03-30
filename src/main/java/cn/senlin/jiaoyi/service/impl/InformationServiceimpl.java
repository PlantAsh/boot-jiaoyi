package cn.senlin.jiaoyi.service.impl;

import cn.senlin.jiaoyi.entity.Article;
import cn.senlin.jiaoyi.entity.InformationCode;
import cn.senlin.jiaoyi.entity.UserInformation;
import cn.senlin.jiaoyi.mapper.ArticleMapper;
import cn.senlin.jiaoyi.mapper.InformationCodeMapper;
import cn.senlin.jiaoyi.mapper.UserInformationMapper;
import cn.senlin.jiaoyi.service.InformationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("informationService")
public class InformationServiceimpl implements InformationService {
	@Resource
	private UserInformationMapper userInformationMapper;
	@Resource
	private InformationCodeMapper informationCodeMapper;
	@Resource
	private ArticleMapper articleMapper;

	public UserInformation loadInformation(String userAccount) {
		
		return userInformationMapper.loadInformation(userAccount);
	}

	/**
	 * 获取字典表
	 *
	 * @param codeType
	 * @return
	 */
	public List<InformationCode> loadByType(String codeType) {
		return informationCodeMapper.loadByType(codeType);
	}
	
	public String updateInformation(UserInformation usin, String userName) {
		if(!usin.getUserName().equals(userName)) {
			Article ar = new Article();
			ar.setUserAccount(usin.getUserAccount());
			ar.setUserName(usin.getUserName());
			int j = articleMapper.updateUserName(ar);
		}
		int i = userInformationMapper.updateInformationSelective(usin);
		return "success";
	}

}
