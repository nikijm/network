package com.jk.ndtetl.mon.dao;

import java.util.List;

import com.jk.ndtetl.mon.ServerInfo;
import com.jk.ndtetl.mon.ServerInfoQuery;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控信息的dao
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServerInfoDao {

    /**
     * 保存监控信息
     * 
     * @param serverInfo
     */
    public void insertServerInfo(ServerInfo serverInfo);

    /**
     * 查询监控信息列表
     * 
     * @param serverInfoQuery
     * @return
     */
    public List<ServerInfo> getServerInfo(ServerInfoQuery serverInfoQuery);

    /**
     * 
     * @param serverInfoQuery
     * @return
     */
    public long selectServerInfoCount(ServerInfoQuery serverInfoQuery);

    public long selectOneServerInfoCount(ServerInfoQuery queryOneDay);

    /**
     * 获取主机CPU的监控信息
     * 
     * @param tablename
     * @param monitorCompunent
     * @param dateTime
     * @return
     */
    public List<Long> getMonitorCpuInfo(ServerInfoQuery serverInfoQuery);

    /**
     * 获取主机内存的监控信息
     * 
     * @param tablename
     * @param monitorCompunent
     * @param dateTime
     * @return
     */
    public List<Long> getMonitorMemoryInfo(ServerInfoQuery serverInfoQuery);

    /**
     * 获取主机磁盘的监控信息
     * 
     * @param tablename
     * @param monitorCompunent
     * @param dateTime
     * @return
     */
    public List<Long> getMonitorDiskInfo(ServerInfoQuery serverInfoQuery);

    /**
     * 查询监控信息的条数
     * 
     * @param serverInfoQuery
     * @return
     */
    public long getMonitorCpuInfoCount(ServerInfoQuery serverInfoQuery);

    /**
     * 查询监控信息的条数
     * 
     * @param serverInfoQuery
     * @return
     */
    public long getMonitorMemoryInfoCount(ServerInfoQuery serverInfoQuery);

    /**
     * 查询监控信息的条数
     * 
     * @param serverInfoQuery
     * @return
     */
    public long getMonitorDiskInfoCount(ServerInfoQuery serverInfoQuery);

    /**
     * 查询监控的时间
     * 
     * @param serverInfoQuery
     * @return
     */
    public List<String> getMonitorCpuInfoTime(ServerInfoQuery serverInfoQuery);

    /**
     * 查询监控的时间
     * 
     * @param serverInfoQuery
     * @return
     */
    public List<String> getMonitorDiskInfoTime(ServerInfoQuery serverInfoQuery);

    /**
     * 查询监控的时间
     * 
     * @param serverInfoQuery
     * @return
     */
    public List<String> getMonitorMemoryInfoTime(ServerInfoQuery serverInfoQuery);

    /**
     * 删除15天之前的监控信息
     * 
     * @param serverInfo
     */
    public void deleteMonitorInfoLtFifteen(ServerInfo serverInfo);

}
