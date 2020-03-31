package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.entity.Article;
import cn.senlin.jiaoyi.entity.TradingInformation;
import cn.senlin.jiaoyi.entity.UserInformation;
import cn.senlin.jiaoyi.service.ArticleService;
import cn.senlin.jiaoyi.service.InformationService;
import cn.senlin.jiaoyi.service.TradingService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/trading")
@SessionAttributes({ "estimate", "tradingId", "otherUser" })
public class TradingController {
	
	@Resource
	private TradingService tradingService;
	
	@Resource
	private ArticleService articleService;
	
	@Resource
	private InformationService informationService;
	
	@RequestMapping(value = "/addTrading", method = RequestMethod.POST)
	public String addTrading(HttpSession session, HttpServletResponse response, int articleId) {
		try {
			String userAccount = (String) session.getAttribute("userAccount");
			Article ar;
			ar = articleService.getArticle(articleId);
			TradingInformation trading = new TradingInformation();
			trading.setTradingAccount(userAccount);
			trading.setArticleId(articleId);
			trading.setUserAccount(ar.getUserAccount());
			trading.setTradingPrice(ar.getArticlePrice());
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			trading.setTradingDate(sdf.format(date));
			trading.setTradingState("待同意");
			String result = tradingService.addTrading(trading);
			boolean flag = false;
			if(result.equals("success")) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
	        response.flushBuffer();
	        response.getWriter().write(json);
	        response.getWriter().flush();
	        response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/getState", method = RequestMethod.POST)
	public String getState(HttpSession session, HttpServletResponse response, int articleId) {
		try {
			String userAccount = (String) session.getAttribute("userAccount");
			String result = tradingService.getState(articleId, userAccount);
			boolean flag = false;
			if(result.equals("待同意")) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
	        response.flushBuffer();
	        response.getWriter().write(json);
	        response.getWriter().flush();
	        response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/trading_ifm", method = RequestMethod.POST)
	public String trading_ifm(HttpSession session, HttpServletResponse response) {
		try {
			String userAccount = (String) session.getAttribute("userAccount");
			List<TradingInformation> tra;
			tra = tradingService.trading_ifm(userAccount);
			Map<String, Object> map = new HashMap<>();
			map.put("trading_ifm", tra);
			map.put("userAccount", userAccount);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
			response.flushBuffer();
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/update_trd", method = RequestMethod.POST)
	public String update_trd(HttpServletResponse response, String state, int tradingId, int articleId) {
		try {
			TradingInformation tr = new TradingInformation();
			tr.setTradingId(tradingId);
			tr.setArticleId(articleId);
			tr.setTradingState(state);
			String result = tradingService.update_trd(tr);
			boolean flag = false;
			if(result.equals("success")) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
			response.flushBuffer();
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/getEstimate", method = RequestMethod.POST)
	public String getEstimate(HttpServletResponse response, ModelMap model, int tradingId) {
		try {
			TradingInformation tr;
			tr = tradingService.getEstimate(tradingId);
			model.addAttribute("estimate", tr);
			model.addAttribute("tradingId", tradingId);
			Map<String, Object> map = new HashMap<>();
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
			response.flushBuffer();
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/estimate", method = RequestMethod.POST)
	public String estimate(HttpSession session, HttpServletResponse response, TradingInformation tr) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
		
		try {
			int tradingId = (Integer) session.getAttribute("tradingId");
			tr.setTradingId(tradingId);
//			if(tr.getBuyEstimate() != null) {
//				String buy = tr.getBuyEstimate();
//				buy = buy.replace("\r\n", "<br/>");
//				tr.setBuyEstimate(buy);
//			}
//			if(tr.getSellEstimate() != null) {
//				String sell = tr.getSellEstimate();
//				sell = sell.replace("\r\n", "<br/>");
//				tr.setSellEstimate(sell);
//			}
			String result = tradingService.update_Estimate(tr);
			if(!result.equals("success")) {
				out.print("<script>alert('服务器错误')</script>");
				out.flush();
			}
			return "user/user_level1";
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/get_allEstimate")
	public String get_allEstimate(HttpServletResponse response, ModelMap model, String otherAccount) {
		try {
			UserInformation usin;
			usin = informationService.loadInformation(otherAccount);
			model.addAttribute("otherUser", usin);
			List<TradingInformation> tr;
			tr = tradingService.get_allEstimate(otherAccount);
			boolean flag = false;
			if(tr.size() > 0) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			map.put("allEstimate", tr);
			map.put("otherAccount", otherAccount);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
			response.flushBuffer();
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
