package cn.senlin.jiaoyi.service.impl;

import cn.senlin.jiaoyi.entity.TradingInformation;
import cn.senlin.jiaoyi.mapper.ArticleMapper;
import cn.senlin.jiaoyi.mapper.TradingInformationMapper;
import cn.senlin.jiaoyi.service.TradingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("tradingService")
public class TradingServiceimpl implements TradingService {

	@Resource
	private TradingInformationMapper tradingInformationMapper;
	
	@Resource
	private ArticleMapper articleMapper;
	
	public String addTrading(TradingInformation trading) {
		
		int i = tradingInformationMapper.addTrading(trading);
		if(i == 0) {
			return "服务器错误";
		} else {
			return "success";
		}
	}

	public String getState(int articleId, String userAccount) {
		
		TradingInformation tr = tradingInformationMapper.getState(articleId, userAccount);
		if (tr == null) {
			return "未交易";
		}
		return tr.getTradingState();
	}

	public List<TradingInformation> trading_ifm(String userAccount) {
		return tradingInformationMapper.trading_ifm(userAccount);
	}

	public String update_trd(TradingInformation trading) {
		
		if(trading.getTradingState().equals("正在交易")) {
			int i = tradingInformationMapper.updateTrading_ifm(trading);
			if(i == 0) {
				return "服务器错误";
			} else {
				tradingInformationMapper.updateTrading_other(trading);
				articleMapper.updateState(trading.getArticleId());
			}
			return "success";
		} else {
			int i = tradingInformationMapper.updateTrading_ifm(trading);
			if(i == 0) {
				return "服务器错误";
			}
			return "success";
		}
	}

	public TradingInformation getEstimate(int tradingId) {
		return tradingInformationMapper.getEstimate(tradingId);
	}

	public String update_Estimate(TradingInformation trading) {
		
		int i = tradingInformationMapper.update_Estimate(trading);
		if(i == 0) {
			return "服务器错误";
		}
		return "success";
	}

	public List<TradingInformation> get_allEstimate(String otherAccount) {
		return tradingInformationMapper.get_allEstimate(otherAccount);
	}

}
