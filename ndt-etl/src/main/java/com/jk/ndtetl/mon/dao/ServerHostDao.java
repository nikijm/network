package com.jk.ndtetl.mon.dao;

import java.util.List;

import com.jk.ndtetl.mon.MonitorTableInfo;
import com.jk.ndtetl.mon.ServerHost;

/**
 * 
 * @ClassName: ServerHostDao
 * @Description: 服务主机的dao
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServerHostDao {

    /**
     * 通过ip查询主机信息
     * 
     * @param ipAddress
     * @return
     */
    public ServerHost getServerHostListByIp(String ipAddress);

    /**
     * 获取可用主机的列表
     * 
     * @return
     */
    public List<ServerHost> listServerhostInfoWithStatu();

    /**
     * 获取所有主机的列表
     * 
     * @return
     */
    public List<ServerHost> listServerhostInfoNotStatu();

    /**
     * 跟新主机的信息
     * 
     * @param serverHost
     */
    public void updateServerhostInfo(ServerHost serverHost);

    /**
     * 插入主机的信息
     * 
     * @param serverHost
     */
    public void insertServerHost(ServerHost serverHost);

    /**
     * 创建表
     * 
     * @param monitorTableInfo
     */
    public void createTable(MonitorTableInfo monitorTableInfo);

    /**
     * 创建序列
     * 
     * @param monitorTableInfo
     */
    public void createSeq(MonitorTableInfo monitorTableInfo);

    /**
     * 创建触发器
     * 
     * @param monitorTableInfo
     */
    public void createTrigger(MonitorTableInfo monitorTableInfo);

    /**
     * 查询一个主机的信息
     * 
     * @param serverId
     * @return
     */
    public ServerHost getServerhostInfo(Integer serverId);

    /**
     * 删除主机的信息
     * 
     * @param id
     */
    public void deleteServerHostInfo(Integer id);

    /**
     * 删除表
     * 
     * @param tablename
     */
    public void deleteServerHostTable(String tablename);

    /**
     * 删除序列
     * 
     * @param tablename
     */
    public void deleteServerHostSequese(String tablename);

}
