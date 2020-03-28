package cn.senlin.jiaoyi.mapper;

import cn.senlin.jiaoyi.entity.UserInformation;

public interface UserInformationMapper {

    void addSelective(UserInformation record);

    UserInformation loadInformation(String userAccount);

    int updateInformationSelective(UserInformation record);
    
}