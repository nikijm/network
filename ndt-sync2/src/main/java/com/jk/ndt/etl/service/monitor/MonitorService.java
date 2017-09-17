package com.jk.ndt.etl.service.monitor;

import java.util.List;

import com.jk.ndt.etl.entity.monitor.AlarmLogs;
import com.jk.ndt.etl.entity.monitor.AlarmRuleBean;
import com.jk.ndt.etl.entity.monitor.Email;
import com.jk.ndt.etl.entity.monitor.MemoryClear;
import com.jk.ndt.etl.entity.monitor.ServerHost;
import com.jk.ndt.etl.entity.monitor.ServerInfo;
import com.jk.ndt.etl.entity.monitor.ServerInfoQuery;

public interface MonitorService {

	// public List<ServerInfo> getAllServerInfo(String classification,String
	// tablename);

	/**
	 * 查询7天
	 * 
	 * @return
	 */
	public ServerInfoQuery querySevenDay();

	/**
	 * 查询一天
	 * 
	 * @return
	 */
	public ServerInfoQuery queryOneDay();

	/**
	 * 查询15天
	 * 
	 * @return
	 */
	public ServerInfoQuery queryOneHour();

	/**
	 * 获取监控的信息
	 * 
	 * @param ipAddress
	 * @param username
	 * @param password
	 * @return
	 */
	public ServerInfo getinfo(String ipAddress, String username, String password);

	/**
	 * 获取监控的信息
	 * 
	 * @param ipAddress
	 * @param username
	 * @param password
	 * @return
	 */
	public ServerInfo getLinuxinfo(String ipAddress, String username, String password);

	// public Long selectServerInfoCount(String classification,String
	// tablename);

	// public List<ServerInfo> getOneServerInfo(String classification, String
	// tablename,Long newDateTime);

	// public long selectOneServerInfoCount(String classification, String
	// tablename, Long newDateTime);
	/**
	 * 获取主机的监控信息
	 * 
	 * @param tablename
	 * @param monitorCompunent
	 * @param dateTime
	 * @return
	 */
	public List<Long> getMonitorInfo(String tablename, String monitorCompunent, String dateTime);

	/**
	 * 获取监控主机的监控时间
	 * 
	 * @param tablename
	 * @param monitorCompunent
	 * @param dateTime
	 * @return
	 */
	public List<String> getMonitorInfoTime(String tablename, String monitorCompunent, String dateTime);

	/**
	 * 获取主机的监控的信息的条是
	 * 
	 * @param tablename
	 * @param monitorCompunent
	 * @param dateTime
	 * @return
	 */
	public long selectMonitorInfoCount(String tablename, String monitorCompunent, String filter_by);

	/**
	 * 清理缓存
	 * 
	 * @param oneServerHost
	 * @return
	 */
	public MemoryClear clearMemoryInfo(ServerHost oneServerHost);

	/**
	 * 磁盘告警策略
	 * 
	 * @param serverHost
	 * @param cpuUsage
	 * @param alarmRule
	 */
	public void alarmRuleDisk(ServerHost serverHost, Integer diskUsage, AlarmRuleBean alarmRule);

	/**
	 * 内存告警策略
	 * 
	 * @param serverHost
	 * @param cpuUsage
	 * @param alarmRule
	 */
	public void alarmRuleMemory(ServerHost serverHost, Integer memoryUsage, AlarmRuleBean alarmRule);

	/**
	 * cpu告警策略
	 * 
	 * @param serverHost
	 * @param cpuUsage
	 * @param alarmRule
	 */
	public void alarmRuleCpu(ServerHost serverHost, Integer cpuUsage, AlarmRuleBean alarmRule);

	/**
	 * 日志告警策略
	 * 
	 * @param serverHost
	 * @param cpuUsage
	 * @param alarmRule
	 */
	public void alarmRuleLogs(ServerHost serverHost, AlarmRuleBean alarmRule);

	/**
	 * 发送邮件
	 * 
	 * @param serverHost
	 * @param alarmRule
	 * @param sendMailInfo
	 * @param content
	 */
	public void simpleEmailAlarm(ServerHost serverHost, AlarmRuleBean alarmRule, Email sendMailInfo, String content);

	/**
	 * 重复发送邮件
	 * 
	 * @param serverHost
	 * @param alarmRule
	 * @param sendMailInfo
	 * @param content
	 */
	public void repeatEmailAlarm(ServerHost serverHost, AlarmRuleBean alarmRule, Email sendMailInfo, String content);

}
