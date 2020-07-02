package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.entity.Article;
import cn.senlin.jiaoyi.entity.TradingInformation;
import cn.senlin.jiaoyi.entity.UserInformation;
import cn.senlin.jiaoyi.entity.util.Reply;
import cn.senlin.jiaoyi.enums.SystemConstantEnum;
import cn.senlin.jiaoyi.service.ArticleService;
import cn.senlin.jiaoyi.service.InformationService;
import cn.senlin.jiaoyi.service.TradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
	Logger log = LoggerFactory.getLogger(TradingController.class);
	
	@Resource
	private TradingService tradingService;
	
	@Resource
	private ArticleService articleService;
	
	@Resource
	private InformationService informationService;

	/**
	 * 添加交易信息
	 *
	 * @param session
	 * @param articleId
	 * @return
	 */
	@PostMapping(value = "/addTrading")
	@ResponseBody
	public Reply addTrading(HttpSession session, int articleId) {
		try {
			String userAccount = (String) session.getAttribute(SystemConstantEnum.USER_ACCOUNT.getCode());
			Article article = articleService.getArticle(articleId);
			TradingInformation trading = new TradingInformation();
			trading.setTradingAccount(userAccount);
			trading.setArticleId(articleId);
			trading.setUserAccount(article.getUserAccount());
			trading.setTradingPrice(article.getArticlePrice());
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

			return Reply.ok(map);
		} catch(Exception e) {
			log.error("添加交易信息报错：", e);
			return Reply.fail("添加交易信息有误！");
		}
	}

	/**
	 * 获取交易状态
	 *
	 * @param session
	 * @param articleId
	 * @return
	 */
	@PostMapping(value = "/getState")
	@ResponseBody
	public Reply getState(HttpSession session, int articleId) {
		try {
			String userAccount = (String) session.getAttribute("userAccount");
			String result = tradingService.getState(articleId, userAccount);
			boolean flag = false;
			if(result.equals("待同意")) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);

			return Reply.ok(map);
		} catch(Exception e) {
			log.error("获取交易状态报错：", e);
			return Reply.fail("获取交易状态有误！");
		}
	}

	/**
	 * 获取主页交易记录
	 *
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/trading_ifm")
	@ResponseBody
	public Reply trading_ifm(HttpSession session) {
		try {
			String userAccount = (String) session.getAttribute("userAccount");
			List<TradingInformation> tra;
			tra = tradingService.trading_ifm(userAccount);
			Map<String, Object> map = new HashMap<>();
			map.put("trading_ifm", tra);
			map.put("userAccount", userAccount);

			return Reply.ok(map);
		} catch(Exception e) {
			log.error("获取主页交易记录报错：", e);
			return Reply.fail("获取主页交易记录有误！");
		}
	}

	/**
	 * 修改交易状态
	 *
	 * @param tradingInformation
	 * @return
	 */
	@PostMapping(value = "/update_trd")
	@ResponseBody
	public Reply update_trd(@RequestBody TradingInformation tradingInformation) {
		try {
			String result = tradingService.update_trd(tradingInformation);
			boolean flag = false;
			if(result.equals("success")) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);

			return Reply.ok(map);
		} catch(Exception e) {
			log.error("修改交易状态报错：", e);
			return Reply.fail("修改交易状态有误！");
		}
	}

	/**
	 * 获取交易评价
	 *
	 * @param session
	 * @param tradingId
	 * @return
	 */
	@PostMapping(value = "/getEstimate")
	public Reply getEstimate(HttpSession session, int tradingId) {
		try {
			TradingInformation estimate = tradingService.getEstimate(tradingId);
			session.setAttribute("estimate", estimate);
			session.setAttribute("tradingId", tradingId);

			return Reply.ok();
		} catch(Exception e) {
			log.error("获取交易评价报错：", e);
			return Reply.fail("获取交易评价有误！");
		}
	}

	/**
	 * 评价交易
	 *
	 * @param session
	 * @param response
	 * @param tradingInformation
	 * @return
	 */
	@PostMapping(value = "/estimate")
	public String estimate(HttpSession session, HttpServletResponse response, TradingInformation tradingInformation) {
		response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

		try {
			PrintWriter out = response.getWriter();

			int tradingId = (Integer) session.getAttribute("tradingId");
			tradingInformation.setTradingId(tradingId);
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
			String result = tradingService.update_Estimate(tradingInformation);
			if(!result.equals("success")) {
				out.print("<script>alert('服务器错误')</script>");
				out.flush();
			}
			return "user/user_level1";
		} catch(Exception e) {
			log.error("评价交易报错：", e);
			return "user/user_level1";
		}
	}

	/**
	 * 获取交易完成记录
	 *
	 * @param session
	 * @param otherAccount
	 * @return
	 */
	@PostMapping(value = "/get_allEstimate")
	@ResponseBody
	public Reply get_allEstimate(HttpSession session, String otherAccount) {
		try {
			UserInformation usin;
			usin = informationService.loadInformation(otherAccount);
			session.setAttribute("otherUser", usin);

			List<TradingInformation> informationList = tradingService.get_allEstimate(otherAccount);
			boolean flag = false;
			if(informationList.size() > 0) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			map.put("allEstimate", informationList);
			map.put("otherAccount", otherAccount);

			return Reply.ok(map);
		} catch(Exception e) {
			log.error("获取交易完成记录报错：", e);
			return Reply.fail("获取交易完成记录有误！");
		}
	}

}
