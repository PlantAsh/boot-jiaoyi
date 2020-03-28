package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.Article;

import java.util.List;

public interface ArticleService {
	public List<Article> loadArticle(Article article, int first, int number) throws Exception;
	
	public int getPage(Article article) throws Exception;
	
	public String addArticle(Article article) throws Exception;
	
	public String deleteArticle(Integer articleId) throws Exception;
	
	public Article getArticle(int articleId) throws Exception;
	
	public String updateArticle(Article article) throws Exception;

}
