package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.TradingInformation;

import java.util.List;

public interface TradingService {
	String addTrading(TradingInformation trading);
	
	String getState(int articleId, String userAccount);
	
	List<TradingInformation> trading_ifm(String userAccount);
	
	String update_trd(TradingInformation trading);
	
	TradingInformation getEstimate(int tradingId);
	
	String update_Estimate(TradingInformation trading);
	
	List<TradingInformation> get_allEstimate(String otherAccount);

}
