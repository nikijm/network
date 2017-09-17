package com.jk.ndt.etl.exception;

/**
 * 
 * @ClassName: LogicalException 
 * @Description: 自定义异常处理类，用于逻辑异常的捕获和处理
 * @author fangwei 
 * @date 2017年5月20日 上午11:12:13 
 *
 */
public class LogicalException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public LogicalException() {
        super();
    }

    public LogicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicalException(String message) {
        super(message);
    }
    
}
