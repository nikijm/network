package com.jk.ndt.etl.dao.monitor;

import java.util.List;

import com.jk.ndt.etl.entity.monitor.ServerInfo;
import com.jk.ndt.etl.entity.monitor.ServerInfoQuery;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控信息的dao
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServerInfoDao {

	public void insertServerInfo(ServerInfo serverInfo);

	public List<ServerInfo> getServerInfo(ServerInfoQuery serverInfoQuery);

	public long selectServerInfoCount(ServerInfoQuery serverInfoQuery);

	public List<ServerInfo> getOneServerInfo(ServerInfoQuery queryOneHour);

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

	public long getMonitorCpuInfoCount(ServerInfoQuery serverInfoQuery);

	public long getMonitorMemoryInfoCount(ServerInfoQuery serverInfoQuery);

	public long getMonitorDiskInfoCount(ServerInfoQuery serverInfoQuery);

	public List<String> getMonitorCpuInfoTime(ServerInfoQuery serverInfoQuery);

	public List<String> getMonitorDiskInfoTime(ServerInfoQuery serverInfoQuery);

	public List<String> getMonitorMemoryInfoTime(ServerInfoQuery serverInfoQuery);

}
