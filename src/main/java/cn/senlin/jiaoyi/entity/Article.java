package cn.senlin.jiaoyi.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
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

}