package com.dakun.jianzhong.model.vo;

import com.dakun.jianzhong.model.AccountUser;

/**
 * Created by hexingfu on 2017/8/8.
 * 客户端“我的”主页model
 * * 前端需要字段：
 * 名字、头像、标签、是否是vip、粉丝数、简介、创作问题数、创作回答数、
 * 创作文章数、关注问题数、关注用户数、关注文章数、用户id；
 */
public class AccountMine {

    /*用户基本信息*/
    private AccountUser baseInfo;
    /*简介，该字段只有专家和经销商用户有值*/
    private String description;
    /*创作问题数量*/
    private int questionNum;
    /*创作回答数量*/
    private int answerNum;
    /*创作文章数*/
    private int articleNum;
    /*关注问题数*/
    private int followQuestionNum;
    /*关注文章数*/
    private int followArticleNum;
    /*关注的作物列表*/
    private Object cropList;
    private int exampleNum;
    private int followExampleNum;

    public int getExampleNum() {
        return exampleNum;
    }

    public void setExampleNum(int exampleNum) {
        this.exampleNum = exampleNum;
    }

    public int getFollowExampleNum() {
        return followExampleNum;
    }

    public void setFollowExampleNum(int followExampleNum) {
        this.followExampleNum = followExampleNum;
    }

    public Object getCropList() {
        return cropList;
    }

    public void setCropList(Object cropList) {
        this.cropList = cropList;
    }

    public AccountUser getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(AccountUser baseInfo) {
        this.baseInfo = baseInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public int getFollowQuestionNum() {
        return followQuestionNum;
    }

    public void setFollowQuestionNum(int followQuestionNum) {
        this.followQuestionNum = followQuestionNum;
    }

    public int getFollowArticleNum() {
        return followArticleNum;
    }

    public void setFollowArticleNum(int followArticleNum) {
        this.followArticleNum = followArticleNum;
    }
}
