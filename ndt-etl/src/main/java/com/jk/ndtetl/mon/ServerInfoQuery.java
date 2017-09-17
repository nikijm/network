package com.jk.ndtetl.mon;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控信息查询的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class ServerInfoQuery {

    private Long startTime; // 开始时间

    private Long endTime; // 结束时间

    private String tablename; // 表的名称

    private Long newDateTime; // 最新的时间

    private String monitorCompunent; // 监控的资源

    public String getMonitorCompunent() {
        return monitorCompunent;
    }

    public void setMonitorCompunent(String monitorCompunent) {
        this.monitorCompunent = monitorCompunent;
    }

    public Long getNewDateTime() {
        return newDateTime;
    }

    public void setNewDateTime(Long newDateTime) {
        this.newDateTime = newDateTime;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

}
