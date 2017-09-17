package com.jk.ndtetl.mon;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 阅读：朱生
 * ？：hostname对应的get方法仅有一处调用
 *   com.jk.ndtetl.mon.service.impl.MonitorServiceimpl#alarmRuleLogs(com.jk.ndtetl.mon.ServerHost, com.jk.ndtetl.mon.AlarmRuleBean)77行
 *   hostname对应的set方法在java中无调用，在sql没有对应的值映射
 * 时间：2017年7月27日 14:07:10
 * -----------------------------------------------------------------------------------
 * 阅读：朱生
 * ？：hostname主机名是否对应现有界面的机器名(description)？如果是则存在多个字段定义了同一个值
 * ？：现有界面description存储的是机器名，这里注释为主机的描述
 * ？：与之对应的表OM_SERVER无hostname相关字段(+﹏+)~
 * 时间：2017年7月27日 13:44:26
 * -----------------------------------------------------------------------------------
 * @ClassName: ServerInfoDao
 * @Description: 服务主机的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerHost implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;// 表的ID

    private String hostname;// 主机名

    private String ipaddress;// IP地址

    @JSONField(serialize = false)
    private String macaddress;// 电脑的mac地址

    private Integer cpusize;// cpu的大小

    private Double memorysize;// 内存的大小

    private Double disksize;// 磁盘的大小

    private String description;// 主机的描述

    @JSONField(serialize = false)
    private String tablename;// 监控表的表名

    private String username;// 服务器的用户名

    private String password;// 服务器的密码

    private String identification;// 服务器的标志特征

    private Integer statu;// 服务器使用的状态

    private String serverNote;// 服务器的备注

    public String getServerNote() {
        return serverNote;
    }

    public void setServerNote(String serverNote) {
        this.serverNote = serverNote;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCpusize() {
        return cpusize;
    }

    public void setCpusize(Integer cpusize) {
        this.cpusize = cpusize;
    }

    public Double getMemorysize() {
        return memorysize;
    }

    public void setMemorysize(Double memorysize) {
        this.memorysize = memorysize;
    }

    public Double getDisksize() {
        return disksize;
    }

    public void setDisksize(Double disksize) {
        this.disksize = disksize;
    }

}
