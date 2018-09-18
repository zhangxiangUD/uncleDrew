package com.dakun.jianzhong.model.constant;

/**
 * <p>User: wangjie
 * <p>Date: 8/14/2017
 */
public enum State {

    DELETED(-10, "删除"), VALID(1, "有效");

    public final Integer value;

    public final String info;

    State(Integer value, String info) {
        this.value = value;
        this.info = info;
    }
}
