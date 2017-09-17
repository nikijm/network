package com.jk.ndt.etl.service.monitor;

import java.util.List;

import com.jk.ndt.etl.entity.monitor.AlarmLogs;
import com.jk.ndt.etl.entity.monitor.AlarmRuleBean;

public interface AlarmRuleService {

	/**
	 * 获得告警规则的列表
	 * 
	 * @param serverId
	 *            主机的ID
	 * @param type
	 *            资源的类型
	 * @param status
	 *            规则的状态
	 * @return 规则的集合
	 */
	public List<AlarmRuleBean> getAlarmRuleList(Integer serverId, Integer type, Integer status);

	/**
	 * 插叙规则同坐规则ID和服务ID
	 * 
	 * @param serverId
	 * @return
	 */
	public List<AlarmRuleBean> listAlarmIdByServerId(Integer serverId);

	/**
	 * 获取规则
	 * 
	 * @param alarmRuleId
	 * @return 规则的实体类
	 */
	public AlarmRuleBean getAlarmRuleById(Integer alarmRuleId);

	/**
	 * 保持一个规则
	 * 
	 * @param alarmRuleBean
	 * @param machineType
	 */
	public void saveOneAlarmRule(AlarmRuleBean alarmRuleBean, Integer[] machineType);

	/**
	 * 通过资源和表达式的类型查询规则
	 * 
	 * @param alarmRuleBean
	 * @return 规则的实体类
	 */
	public AlarmRuleBean getAlarmRuleByModuleAndType(AlarmRuleBean alarmRuleBean);

	/**
	 * 通过内容查规则
	 * 
	 * @param content
	 * @return
	 */
	public AlarmRuleBean getAlarmRuleByContent(String content);

	/**
	 * 跟新告警规则
	 * 
	 * @param alarmRuleBean
	 */
	public void updateOneAlarmRule(AlarmRuleBean alarmRuleBean, Integer[] machineView);

	/**
	 * 跟新告警规则
	 * 
	 * @param alarmLogs
	 */
	public void updateAlarmRule(AlarmRuleBean alarmRule);

	/**
	 * 插入告警日志
	 * 
	 * @param alarmLogs
	 */
	public void insertAlarmLogs(AlarmLogs alarmLogs);

	/**
	 * 跟新告警日志
	 * 
	 * @param alarmLogs
	 */
	public void updateAlarmLogs(AlarmLogs alarmLogs2);

	/**
	 * 跟新告警日志
	 * 
	 * @param alarmLogs
	 */
	public List<AlarmLogs> getRepeatSendAlarmMailList();

	/**
	 * 删除一个规则
	 * 
	 * @param id
	 */
	public void deleteAlarmRule(Integer id);

}
