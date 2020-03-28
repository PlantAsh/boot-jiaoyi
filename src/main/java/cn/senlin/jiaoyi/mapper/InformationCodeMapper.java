package cn.senlin.jiaoyi.mapper;

import cn.senlin.jiaoyi.entity.InformationCode;

import java.util.List;

public interface InformationCodeMapper {

    List<InformationCode> loadByType(String codeType);
    
}