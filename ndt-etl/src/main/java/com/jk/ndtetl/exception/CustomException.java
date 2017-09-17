package com.jk.ndtetl.exception;

public class CustomException extends Exception {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;
    // 异常信息
    private String message;

    public CustomException(String message) {
        super(message);
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}