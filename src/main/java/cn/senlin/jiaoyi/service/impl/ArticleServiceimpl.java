package cn.senlin.jiaoyi.service.impl;

import cn.senlin.jiaoyi.entity.Article;
import cn.senlin.jiaoyi.mapper.ArticleMapper;
import cn.senlin.jiaoyi.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("articleService")
public class ArticleServiceimpl implements ArticleService {
	
	@Resource
	private ArticleMapper articleMapper;

	/**
	 * 获取某楼未交易记录
	 *
	 * @param article
	 * @param first
	 * @param number
	 * @return
	 */
	public List<Article> loadArticle(Article article, int first, int number) {
		return articleMapper.loadArticle(article.getArticleFloor(), first, number);
	}

	/**
	 * 获取未交易总数
	 *
	 * @param article
	 * @return
	 */
	public int getPage(Article article) {
		return articleMapper.getPage(article.getArticleFloor());
	}

	public String addArticle(Article article) {
		
		int i = articleMapper.addArticle(article);
		if(i == 0) {
			return "数据库错误";
		} else {
			return "success";
		}
	}

	public String deleteArticle(Integer articleId) {
		
		int i = articleMapper.deleteArticle(articleId);
		if(i == 0) {
			return "数据库错误";
		} else {
			return "success";
		}
	}

	public Article getArticle(int articleId) {
		
		return articleMapper.getArticle(articleId);
	}

	public String updateArticle(Article article) {
		int i = articleMapper.updateArticle(article);
		if(i == 0) {
			return "修改失败！";
		} else {
			return "success";
		}
	}

}
