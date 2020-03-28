package cn.senlin.jiaoyi.mapper;

import cn.senlin.jiaoyi.entity.Article;

import java.util.List;

public interface ArticleMapper {

    int addArticle(Article record);

    List<Article> loadArticle(String articleFloor, int first, int number);
    
    Article getArticle(int articleId);

    /**
     * 获取未交易总数
     *
     * @param articleFloor
     * @return
     */
    int getPage(String articleFloor);

    int updateUserName(Article record);
    
    void updateState(int articleId);
    
    int updateArticle(Article record);
    
    int deleteArticle(Integer postsId);
    
}