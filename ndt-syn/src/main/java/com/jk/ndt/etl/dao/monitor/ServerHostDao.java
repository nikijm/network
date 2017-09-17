package com.jk.ndt.etl.dao.monitor;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.web.bind.annotation.PathVariable;

import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.entity.monitor.MonitorTableInfo;
import com.jk.ndt.etl.entity.monitor.ServerHost;

/**
 * 
 * @ClassName: ServerHostDao
 * @Description: 服务主机的dao
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServerHostDao {

	public ServerHost getServerHostListByIp(String ipAddress);

	/**
	 * 获取主机的列表
	 * 
	 * @return
	 */
	public List<ServerHost> listServerhostInfo();

	public void updateServerhostUserInfo(ServerHost serverHost);

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
