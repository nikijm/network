package com.jk.ndt.etl.utility;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: ErrorResult
 * @Description: 对错误响应格式进行统一封装，包含200x，300x和400x错误
 * @author fangwei
 * @date 2017年5月24日 下午5:11:46
 *
 */
public class ErrorUtil {
    private static final String MESSAGE_400 = "客户端输入有误，请确保输入正确";
    private static final String MESSAGE_401 = "没有授权";
    private static final String MESSAGE_40317 = "客户端证书已过期或尚未生效";
    private static final String MESSAGE_403 = "资源不允许访问";
    private static final int ERROR_400 = 400;
    private static final int ERROR_401 = 401;
    private static final int ERROR_403 = 403;
    private static final int ERROR_40317 = 40317;

    private static final String MESSAGE = "message";
    private static final String ERRORS = "errors";

    /**
     * errors:{ "username":"用户名不能为空" "password":"密码不能为空" }
     * 
     * @author 朱生
     * @param errorMessage
     * @return
     */
    public static Map<String, Object> getRequestError(JSONObject errorMessage) {
        return getRequestError(errorMessage, MESSAGE_400);
    }
    /**
     * errors:{ "username":"用户名不能为空" "password":"密码不能为空" }
     *
     * @author 朱生
     * @param errorMessage
     * @return
     */
    public static Map<String, Object> getRequestError(JSONObject errorMessage,HttpServletResponse response,String message) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, MESSAGE_400);
        error.put(ERRORS, errorMessage);
        response.setStatus(ERROR_400);
        if (!StringUtils.isBlank(message)) {
            error.put(MESSAGE, message);
        }
        return error;
    }
    /**
     * 
     * @Description: 请求参数错误信息封装
     * @author fangwei
     * @date 2017年5月26日 下午2:39:08
     * @param errorMessage
     * @param message
     *            用于传递给前端显示，重要
     * @return
     */
    public static Map<String, Object> getRequestError(JSONObject errorMessage, String message) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, message);
        error.put(ERRORS, errorMessage);
        HttpServletResponse response = WebUtils.getResponse();
        response.setStatus(ERROR_400);
        return error;
    }

    public static Map<String, Object> getPermissionError(JSONObject errorMessage) {
        if (null == errorMessage) {
            errorMessage = new JSONObject();
            errorMessage.put("NoPermissionError", "没有授权");
        }
        return getPermissionError(errorMessage, MESSAGE_401);
    }

    /**
     * token过期
     * @param errorMessage
     * @param response
     * @return
     */
    public static Map<String, Object> getOverdueError(JSONObject errorMessage,HttpServletResponse response) {
        if (null == errorMessage) {
            errorMessage = new JSONObject();
            errorMessage.put("NoOverdueError", MESSAGE_40317);
        }
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, MESSAGE_40317);
        error.put(ERRORS, errorMessage);
        response.setStatus(ERROR_40317);
        return error;
    }

    /**
     * 无授权
     * @param errorMessage
     * @param response
     * @return
     */
    public static Map<String, Object> getPermissionError(JSONObject errorMessage,HttpServletResponse response) {
        if (null == errorMessage) {
            errorMessage = new JSONObject();
            errorMessage.put("NoPermissionError", MESSAGE_401);
        }
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, MESSAGE_401);
        error.put(ERRORS, errorMessage);
        response.setStatus(ERROR_401);
        return error;
    }
    /**
     * 
     * @Description: 没有权限错误信息封装
     * @author fangwei
     * @date 2017年5月26日 下午2:39:54
     * @param errorMessage
     * @param message
     *            返回给前端用于展示，重要
     * @return
     */
    public static Map<String, Object> getPermissionError(JSONObject errorMessage, String message) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, message);
        error.put(ERRORS, errorMessage);
        HttpServletResponse response = WebUtils.getResponse();
        response.setStatus(ERROR_401);
        return error;
    }
    public static Map<String, Object> getPermissionError(JSONObject errorMessage, String message,HttpServletResponse response) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, message);
        error.put(ERRORS, errorMessage);
        response.setStatus(ERROR_401);
        return error;
    }
    public static Map<String, Object> getNotAllowError(JSONObject errorMessage,HttpServletResponse response) {
        if (null == errorMessage) {
            errorMessage = new JSONObject();
            errorMessage.put("NotAllowError", "您没有该资源的访问权限");
        }
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, MESSAGE_403);
        error.put(ERRORS, errorMessage);
        response.setStatus(ERROR_403);
        return error;
    }

    public static Map<String, Object> getNotAllowError(JSONObject errorMessage) {
        if (null == errorMessage) {
            errorMessage = new JSONObject();
            errorMessage.put("NotAllowError", "您没有该资源的访问权限");
        }
        return getNotAllowError(errorMessage, MESSAGE_403);
    }

    /**
     * 
     * @Description: 资源不允许访问错误信息封装
     * @author fangwei
     * @date 2017年5月26日 下午2:40:31
     * @param errorMessage
     * @param message
     *            用于给前端显示，重要
     * @return
     */
    public static Map<String, Object> getNotAllowError(JSONObject errorMessage, String message) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(MESSAGE, message);
        error.put(ERRORS, errorMessage);
        HttpServletResponse response = WebUtils.getResponse();
        response.setStatus(ERROR_403);
        return error;
    }

}
