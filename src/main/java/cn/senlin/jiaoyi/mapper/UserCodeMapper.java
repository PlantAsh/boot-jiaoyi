package cn.senlin.jiaoyi.mapper;

import cn.senlin.jiaoyi.entity.UserCode;

public interface UserCodeMapper {
    int deleteByPrimaryKey(Integer codeId);

    int insert(UserCode record);

    int insertSelective(UserCode record);

    UserCode selectByPrimaryKey(Integer codeId);

    int updateByPrimaryKeySelective(UserCode record);

    int updateByPrimaryKey(UserCode record);
}