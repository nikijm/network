package com.jk.ndtetl.dbmeta;

import java.util.Date;

/**
 * 数据日志
 * 
 * @ClassName: DataFileLog
 * @author lianhanwen
 * @date 2017年7月3日 下午1:52:33
 *
 */
public class DataFileLog {

    /**
     * 唯一id，序列递增
     */
    private Integer etlDatafileLogId;

    /**
     * datafile的Id
     */
    private Integer etlDatafileId;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 成功的数量
     */
    private Integer successNum;

    /**
     * 错误的数量
     */
    private Integer errorNum;

    /**
     * 未知的数量
     */

    private Integer unknownNum;

    /**
     * 操作
     */
    private String action;

    /**
     * 状态
     */
    private Integer running;

    /**
     * 处理说明
     */
    private String message;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 创建者
     */
    private Integer createdBy;

    public Integer getEtlDatafileLogId() {
        return etlDatafileLogId;
    }

    public void setEtlDatafileLogId(Integer etlDatafileLogId) {
        this.etlDatafileLogId = etlDatafileLogId;
    }

    public Integer getEtlDatafileId() {
        return etlDatafileId;
    }

    public void setEtlDatafileId(Integer etlDatafileId) {
        this.etlDatafileId = etlDatafileId;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
    }

    public Integer getUnknownNum() {
        return unknownNum;
    }

    public void setUnknownNum(Integer unknownNum) {
        this.unknownNum = unknownNum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

}