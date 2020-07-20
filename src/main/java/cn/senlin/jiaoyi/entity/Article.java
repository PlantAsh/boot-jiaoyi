package cn.senlin.jiaoyi.entity;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
    private static final long serialVersionUID = -902754528539248322L;

    private Integer articleId;

    private String userAccount;
    
    private String userName;

    private String articleName;

    private String articlePrice;

    private String articleDescribe;

    private String articlePicture;

    private String articleState;

    private String articleFloor;

    private Date articleDate;

    private String articleModify;

    private Date modifyDate;

    private String deleteState;
    
    private String date;//作为articleDate的替换属性

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticlePrice() {
        return articlePrice;
    }

    public void setArticlePrice(String articlePrice) {
        this.articlePrice = articlePrice;
    }

    public String getArticleDescribe() {
        return articleDescribe;
    }

    public void setArticleDescribe(String articleDescribe) {
        this.articleDescribe = articleDescribe;
    }

    public String getArticlePicture() {
        return articlePicture;
    }

    public void setArticlePicture(String articlePicture) {
        this.articlePicture = articlePicture;
    }

    public String getArticleState() {
        return articleState;
    }

    public void setArticleState(String articleState) {
        this.articleState = articleState;
    }

    public String getArticleFloor() {
        return articleFloor;
    }

    public void setArticleFloor(String articleFloor) {
        this.articleFloor = articleFloor;
    }

    public Date getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(Date articleDate) {
        this.articleDate = articleDate;
    }

    public String getArticleModify() {
        return articleModify;
    }

    public void setArticleModify(String articleModify) {
        this.articleModify = articleModify;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(String deleteState) {
        this.deleteState = deleteState;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}