package cn.senlin.jiaoyi.mapper;

import cn.senlin.jiaoyi.entity.TradingInformation;

import java.util.List;

public interface TradingInformationMapper {
    List<TradingInformation> trading_ifm(String userAccount);
    
    List<TradingInformation> get_allEstimate(String otherAccount);

    int addTrading(TradingInformation record);

    TradingInformation getState(int articleId, String userAccount);

    int updateTrading_ifm(TradingInformation record);
    
    void updateTrading_other(TradingInformation record);
    
    TradingInformation getEstimate(int tradingId);
    
    int update_Estimate(TradingInformation record);
}