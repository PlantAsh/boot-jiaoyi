package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.entity.Article;
import cn.senlin.jiaoyi.entity.InformationCode;
import cn.senlin.jiaoyi.entity.util.Reply;
import cn.senlin.jiaoyi.service.ArticleService;
import cn.senlin.jiaoyi.service.InformationService;
import cn.senlin.jiaoyi.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
@SessionAttributes({ "articleFloor", "inFloor" })
public class ArticleController {
	Logger log = LoggerFactory.getLogger(ArticleController.class);
	
	@Resource
	private ArticleService articleService;
	@Resource
	private InformationService informationService;
	@Resource
	private PropertiesUtils propertiesUtils;
	
	private int first;
	private int page;
	private int pagenumber;
	private int number;
	private String level;

	/**
	 * 获取第一页未交易记录
	 *
	 * @param session
	 * @param article
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/floor", method = RequestMethod.POST)
	@ResponseBody
	public Reply Floor(HttpSession session, Article article, ModelMap model) {
		try {
			first = 0;
			number = 18;

			model.addAttribute("articleFloor", article.getArticleFloor());
			List<Article> articleList = articleService.loadArticle(article, first, number);
			page = articleService.getPage(article);
			if (page % number > 0) {
				pagenumber = page / number + 1;
			} else {
				pagenumber = page / number;
			}
			boolean flag = false;
			if(articleList.size() > 0) {
				flag = true;
			}

			for (Article ar : articleList) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(ar.getArticleDate());
				ar.setDate(dateString);

				//补全图片地址
				if (!StringUtils.isEmpty(ar.getArticlePicture())) {
					ar.setArticlePicture(propertiesUtils.getAccessUrl() + File.separator + ar.getArticlePicture());
				}
			}
			
			level = (String) session.getAttribute("userLevel");
			Map<String, Object> map = new HashMap<>();
	        map.put("flag", flag);
	        map.put("article", articleList);
	        map.put("pagenumber", pagenumber);
	        map.put("now", first / number + 1);
	        map.put("level", level);

	        return Reply.ok(map);
		} catch (Exception e) {
			log.error("获取第一页未交易记录失败：", e);
			return Reply.fail("获取第一页未交易记录失败！");
		}
	}

	/**
	 * 跳转楼层
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sell")
	public String Load(ModelMap model) {
		try {
			List<InformationCode> inFloor;
			inFloor = informationService.loadByType("floor");
			model.addAttribute("inFloor", inFloor);
			return "trading/sell";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取下一页未交易记录
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/nextPage", method = RequestMethod.POST)
	@ResponseBody
	public Reply nextPage(HttpSession session) {
		try {
			if (first >= (page - number)) {
				return Reply.fail("获取数据失败！");
			} else {
				first = first + number;
				Article art = new Article();
				String articleFloor = (String) session.getAttribute("articleFloor");
				art.setArticleFloor(articleFloor);
				List<Article> articleList = articleService.loadArticle(art, first, number);
				page = articleService.getPage(art);
				if (page % number > 0) {
					pagenumber = page / number + 1;
				} else {
					pagenumber = page / number;
				}
				boolean flag = false;
				if(articleList.size() > 0) {
					flag = true;
				}

				for (Article article : articleList) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateString = formatter.format(article.getArticleDate());
					article.setDate(dateString);

					//补全图片地址
					if (!StringUtils.isEmpty(article.getArticlePicture())) {
						article.setArticlePicture(propertiesUtils.getAccessUrl() + File.separator + article.getArticlePicture());
					}
				}
				
				Map<String, Object> map = new HashMap<>();
		        map.put("flag", flag);
		        map.put("article", articleList);
		        map.put("pagenumber", pagenumber);
		        map.put("now", first / number + 1);
		        map.put("level", level);

		        return Reply.ok(map);
			}
		} catch (Exception e) {
			log.error("获取下一页未交易记录失败：", e);
			return Reply.fail("获取下一页未交易记录失败");
		}
	}

	/**
	 * 获取上一页未交易记录
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/backPage", method = RequestMethod.POST)
	@ResponseBody
	public Reply backPage(HttpSession session) {
		try {
			if (first == 0) {
				return Reply.fail("获取数据失败！");
			} else {
				first = first - number;
				Article art = new Article();
				String articleFloor = (String) session.getAttribute("articleFloor");
				art.setArticleFloor(articleFloor);
				List<Article> articleList = articleService.loadArticle(art, first, number);
				page = articleService.getPage(art);
				if (page % number > 0) {
					pagenumber = page / number + 1;
				} else {
					pagenumber = page / number;
				}
				boolean flag = false;
				if(articleList.size() > 0) {
					flag = true;
				}

				for (Article article : articleList) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateString = formatter.format(article.getArticleDate());
					article.setDate(dateString);

					//补全图片地址
					if (!StringUtils.isEmpty(article.getArticlePicture())) {
						article.setArticlePicture(propertiesUtils.getAccessUrl() + File.separator + article.getArticlePicture());
					}
				}
				
				Map<String, Object> map = new HashMap<>();
		        map.put("flag", flag);
		        map.put("article", articleList);
		        map.put("pagenumber", pagenumber);
		        map.put("now", first / number + 1);
		        map.put("level", level);

		        return Reply.ok(map);
			}
		} catch (Exception e) {
			log.error("获取上一页未交易记录失败：", e);
			return Reply.fail("获取上一页未交易记录失败！");
		}
	}

	/**
	 * 跳转某页
	 *
	 * @param somePage
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/somePage", method = RequestMethod.POST)
	@ResponseBody
	public Reply somePage(@RequestParam(value = "somePage") int somePage, HttpSession session) {
		try {
			first = (somePage - 1) * number;
			Article art = new Article();
			String articleFloor = (String) session.getAttribute("articleFloor");
			art.setArticleFloor(articleFloor);
			List<Article> articleList = articleService.loadArticle(art, first, number);
			page = articleService.getPage(art);
			if (page % number > 0) {
				pagenumber = page / number + 1;
			} else {
				pagenumber = page / number;
			}
			boolean flag = false;
			if(articleList.size() > 0) {
				flag = true;
			}

			for (Article article : articleList) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(article.getArticleDate());
				article.setDate(dateString);

				//补全图片地址
				if (!StringUtils.isEmpty(article.getArticlePicture())) {
					article.setArticlePicture(propertiesUtils.getAccessUrl() + File.separator + article.getArticlePicture());
				}
			}
			
			Map<String, Object> map = new HashMap<>();
	        map.put("flag", flag);
	        map.put("article", articleList);
	        map.put("pagenumber", pagenumber);
	        map.put("now", first / number + 1);
	        map.put("level", level);

	        return Reply.ok(map);
		} catch (Exception e) {
			log.error("跳转页面失败：", e);
			return Reply.fail("跳转页面失败！");
		}
	}

}
