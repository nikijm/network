package com.jk.ndtetl.mon.dao;

import java.util.List;

import com.jk.ndtetl.mon.AlarmLogs;
import com.jk.ndtetl.mon.AlarmRuleBean;

/**
 * 
 * @ClassName: AlarmRuleDao
 * @Description: 告警的dao
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface AlarmRuleDao {

    /**
     * 规则的列表
     * 
     * @param alarmRuleBean
     * @return 规则的集合
     */
    public List<AlarmRuleBean> getAlarmRuleList(AlarmRuleBean alarmRuleBean);

    public List<AlarmRuleBean> listAlarmIdByServerId(Integer serverId);

    public AlarmRuleBean getAlarmRuleById(Integer alarmRuleId);

    /**
     * 保存一个规则
     * 
     * @param alarmRuleBean
     */
    public void addOneAlarmRule(AlarmRuleBean alarmRuleBean);

    /**
     * 通过资源和表达式的类型规则
     * 
     * @param alarmRuleBean
     * @return 规则的实体类
     */
    public AlarmRuleBean getAlarmRuleByModuleAndType(AlarmRuleBean alarmRuleBean);

    public void addServerRuleByRuleId(AlarmRuleBean alarmRuleBean);

    /**
     * 通过内容查规则
     * 
     * @param content
     * @return
     */
    public AlarmRuleBean getAlarmRuleByContent(AlarmRuleBean alarmRuleBean);

    /**
     * 修改一个规则
     * 
     * @param alarmRuleBean
     * @param machineView
     */
    public void updateOneAlarmRule(AlarmRuleBean alarmRuleBean);

    /**
     * 修改日志规则
     * 
     * @param alarmRuleBean
     * @param machineView
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
     * 获得重复发送告警日志的列表
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

    /**
     * 通过ID查询规则
     * 
     * @param alarmRuleId
     * @return 规则的实体对象
     */
    public AlarmRuleBean getAlarmRuleByIdNotStatus(Integer alarmRuleId);

    /**
     * 删除规则通过服务器的ID
     * 
     * @param alarmRuleBean
     */
    public void deleteAlarmRuleByServerId(AlarmRuleBean alarmRuleBean);

    /**
     * 插入业务告警日志
     * 
     * @param alarmLogs
     */
    public void insertBusinessAlarmLogs(AlarmLogs alarmLogs);

}
