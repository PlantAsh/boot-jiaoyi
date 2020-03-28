package cn.senlin.jiaoyi.mapper;

import cn.senlin.jiaoyi.entity.User;

public interface UserMapper {

    void addUser(User record);

    User loadUser(String userAccount);
    
    int updatePassword(User record);
    
}