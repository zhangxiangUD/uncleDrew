package com.dakun.jianzhong.model.vo;

/**
 * Created by hexingfu on 2017/9/19.
 */
public class SocialMineVo {

    private int questionNum;
    private int answerNum;
    private int articleNum;
    private int followQuestionNum;
    private int followArticleNum;
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
