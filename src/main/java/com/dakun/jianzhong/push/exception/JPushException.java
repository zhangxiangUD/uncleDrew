package com.dakun.jianzhong.push.exception;

/**
 * 极光推送异常封装
 * <p>User: wangjie
 * <p>Date: 12/14/2017
 * @author wangjie
 */
public class JPushException extends RuntimeException {

    private int code;

    public JPushException() {
        super();
    }

    public JPushException(int code, String message) {
        super(message);
        this.code = code;
    }

    public JPushException(String message, Throwable cause) {
        super(message, cause);
    }

    public JPushException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public JPushException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "[Error Code]:" + code + ", "
                + "[Message]:" + getMessage();
    }
}
