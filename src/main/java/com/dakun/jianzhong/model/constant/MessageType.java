package com.dakun.jianzhong.model.constant;

/**
 * 1-系统消息  2-新关注用户 3-新关注问题
 * 4-新关注文章 5-新回答（问题） 6-新评论（文章）
 * 7-新对话（问题） 8-新对话（文章） 9-新点赞（问题回答）
 * 10-新点赞（文章评论） 11-奖品发货通知
 * Created by hexingfu on 2017/9/8.
 */
public enum MessageType {
    SYSTEM(1,"系统消息"),
    FOLLOW_USER(2,"新关注用户"),
    FOLLOW_QUESTION(3,"新关注问题"),
    FOLLOW_ARTICLE(4,"新关注文章"),
    ANSWER_QUESTION(5,"新回答（问题）"),
    COMMENT_ARTICLE(6,"新评论（文章）"),
    CONVERSATION_QUESTION(7,"新对话（问题）"),
    CONVERSATION_ARTICLE(8,"新对话（文章）"),
    AGREE_QUESTION(9,"新点赞（问题回答）"),
    AGREE_ARTICLE(10,"新点赞（文章评论）"),
    SEND_LOTTERY(11,"奖品发货通知"),
    TAG_CORRECTION(12,"tag纠错回复"),
    COMMENT_EXAMPLE(13,"新评论（示范）"),
    AGREE_EXAMPLE(14,"新点赞（示范评论）"),
    CONVERSATION_EXAMPLE(15,"新对话（示范）"),;
    private int state;
    private String msg;
    MessageType(int state,String msg){
        this.state = state;
        this.msg = msg;
    }

    public int value() {
        return state;
    }
}