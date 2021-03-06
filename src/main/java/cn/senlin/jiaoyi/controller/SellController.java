package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.entity.Article;
import cn.senlin.jiaoyi.entity.UserInformation;
import cn.senlin.jiaoyi.service.ArticleService;
import cn.senlin.jiaoyi.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/sell")
@SessionAttributes({ "article" })
public class SellController {
	Logger log = LoggerFactory.getLogger(SellController.class);

	@Resource
	private ArticleService articleService;
	@Resource
	private PropertiesUtils propertiesUtils;
	@Resource
	private KafkaTemplate<Object, Object> kafkaTemplate;

	/**
	 * 新增商品
	 *
	 * @param session
	 * @param response
	 * @param request
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addArticle", method = RequestMethod.POST)
	public String addArticle(HttpSession session, HttpServletResponse response, HttpServletRequest request, Article article) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			Date date = new Date();
			article.setArticleDate(date);
			article.setArticleState("未交易");
			UserInformation usin = (UserInformation) session.getAttribute("userInformation");
			article.setUserAccount(usin.getUserAccount());
			article.setUserName(usin.getUserName());
			// 转型为MultipartHttpRequest(重点的所在)
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 获得第1张图片（根据前台的name名称得到上传的文件）
			MultipartFile imgFile = multipartRequest.getFile("Picture");
			if (imgFile == null) {
				out.print("<script>alert('图片上传失败！')</script>");
				out.flush();
				return "trading/article_information";
			}
			// 保存第一张图片
			String file = "error";
			if (!(imgFile.getOriginalFilename() == null || "".equals(imgFile.getOriginalFilename()))) {
				file = this.addPicture(imgFile,usin.getUserAccount());
			}

			if(file.equals("error")) {
				out.print("<script>alert('图片格式错误！')</script>");
				out.flush();
			}
			if(!file.equals("error")) {
				article.setArticlePicture(file);
			}
			if(article.getArticleDescribe() != null) {
				String text = article.getArticleDescribe();
				text = text.replace("\r\n", "<br/>");
				article.setArticleDescribe(text);
			}
			String result = articleService.addArticle(article);
			if(!result.equals("success")) {
				out.print("<script>alert('" + result + "')</script>");
				out.flush();
			}
			return "trading/article";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取商品信息
	 *
	 * @param session
	 * @param response
	 * @param articleId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getArticle")
	public String getArticle(HttpSession session, HttpServletResponse response, int articleId, ModelMap model) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			Article article = articleService.getArticle(articleId);
			if (article == null) {
				out.print("<script>alert('无此物品')</script>");
				out.flush();
				return "/trading/article";
			} else {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(article.getArticleDate());
				article.setDate(dateString);
				if (!StringUtils.isEmpty(article.getArticlePicture())) {
					article.setArticlePicture(propertiesUtils.getAccessUrl() + File.separator + article.getArticlePicture());
				}
				model.addAttribute("article", article);
				return "/trading/article_information";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * 修改商品信息
	 *
	 * @param session
	 * @param response
	 * @param request
	 * @param article
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
	public String updateArticle(HttpSession session, HttpServletResponse response, HttpServletRequest request, @RequestBody Article article, ModelMap model) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			Article ar = (Article) session.getAttribute("article");
			article.setArticleId(ar.getArticleId());
			Date date = new Date();
			article.setModifyDate(date);
			String userAccount = (String) session.getAttribute("userAccount");
			article.setArticleModify(userAccount);
			// 转型为MultipartHttpRequest(重点的所在)
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 获得第1张图片（根据前台的name名称得到上传的文件）
			MultipartFile imgFile = multipartRequest.getFile("Picture");
			if (imgFile == null) {
				out.print("<script>alert('图片上传失败！')</script>");
				out.flush();
				return "trading/article_information";
			}
			// 保存第一张图片
			String file = "";
			if (!(imgFile.getOriginalFilename() == null || "".equals(imgFile.getOriginalFilename()))) {
				file = this.addPicture(imgFile,userAccount);
			}

			if(file.equals("error")) {
				out.print("<script>alert('图片格式错误！')</script>");
				out.flush();
			}
			if(!file.equals("error")) {
				article.setArticlePicture(file);
			}
			if(article.getArticleDescribe() != null) {
				String text = article.getArticleDescribe();
				text = text.replace("\r\n", "<br/>");
				article.setArticleDescribe(text);
			}
			String result = articleService.updateArticle(article);
			if(!result.equals("success")) {
				out.print("<script>alert('" + result + "')</script>");
				out.flush();
			} else {
				model.addAttribute("article", article);
			}
			return "trading/article_information";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除商品
	 *
	 * @param session
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteArticle")
	public String deleteArticle(HttpSession session, HttpServletResponse response, ModelMap model) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			Article ar = (Article) session.getAttribute("article");
			String result = articleService.deleteArticle(ar.getArticleId());
			if(!result.equals("success")) {
				out.print("<script>alert('" + result + "')</script>");
				out.flush();
			}
			return "trading/article";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("/kafka")
	@ResponseBody
	void sendKafka() {
		kafkaTemplate.send("wusen", "123");
	}

	@KafkaListener(id = "webGroup", topics = "wusen")
	public void listen(String input) {
		log.info("input value: {}", input);
	}

	/*--------------------------------------------------------------------------*/

	/**
	 * 存储图片
	 *
	 * @param imgFile
	 * @param userAccount
	 * @return
	 */
	private String addPicture(MultipartFile imgFile, String userAccount) {
		try {
			String fileName = imgFile.getOriginalFilename();
			if (StringUtils.isEmpty(fileName)) {
				return "error";
			}
			// 获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			// 对扩展名进行小写转换
			ext = ext.toLowerCase();
			// 定义一个数组，用于保存可上传的文件类型
			List<String> fileTypes = new ArrayList<>();
			fileTypes.add("jpg");
			fileTypes.add("jpeg");
			fileTypes.add("bmp");
			fileTypes.add("gif");
			fileTypes.add("png");

			File file;
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS_");
			String dateString = formatter.format(date);
			String newName = dateString + fileName;
			if (fileTypes.contains(ext)) { // 如果扩展名属于允许上传的类型，则创建文件
				String filePath = propertiesUtils.getFilePath() + File.separator + "article_image";
				File secondFolder = new File(filePath, userAccount);
	            if(!secondFolder.exists()) { //如果二级文件夹不存在，则创建二级文件夹
	                secondFolder.mkdirs();
				}
				file = new File(secondFolder, newName);    //创建完二级文件夹后，再合建文件
				imgFile.transferTo(file); // 保存上传的文件
				return "article_image" + File.separator + userAccount + File.separator + newName;
			} else {
				return "error";
			}
		} catch(Exception e) {
			log.error("存储图片失败：", e);
			return "error";
		}
		
	}
}
