package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.TradingInformation;

import java.util.List;

public interface TradingService {
	public String addTrading(TradingInformation trading) throws Exception;
	
	public String getState(int articleId, String userAccount) throws Exception;
	
	public List<TradingInformation> trading_ifm(String userAccount) throws Exception;
	
	public String update_trd(TradingInformation trading) throws Exception;
	
	public TradingInformation getEstimate(int tradingId) throws Exception;
	
	public String update_Estimate(TradingInformation trading) throws Exception;
	
	public List<TradingInformation> get_allEstimate(String otherAccount) throws Exception;

}
