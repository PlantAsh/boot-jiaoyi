package cn.senlin.jiaoyi.service;

import cn.senlin.jiaoyi.entity.Article;

import java.util.List;

public interface ArticleService {
	List<Article> loadArticle(Article article, int first, int number);
	
	int getPage(Article article);
	
	String addArticle(Article article);
	
	String deleteArticle(Integer articleId);
	
	Article getArticle(int articleId);
	
	String updateArticle(Article article);

}
