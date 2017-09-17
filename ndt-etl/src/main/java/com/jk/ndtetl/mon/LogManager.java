package com.jk.ndtetl.mon;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * @ClassName: LogManager
 * @Description: 日志管理的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LogManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id; // 表的ID

    @JSONField(serialize = false)
    private Long dateTime; // 创建时间

    private Integer logLevel; // 日志级别

    private String descriptionLog; // 日志描述

    private String requestUri;

    private String userName;

    private String dateTimeView;

    public String getDateTimeView() {
        return dateTimeView;
    }

    public void setDateTimeView(String dateTimeView) {
        this.dateTimeView = dateTimeView;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public String getDescriptionLog() {
        return descriptionLog;
    }

    public void setDescriptionLog(String descriptionLog) {
        this.descriptionLog = descriptionLog;
    }

}
