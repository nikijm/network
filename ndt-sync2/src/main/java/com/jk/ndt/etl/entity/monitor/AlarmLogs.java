package com.jk.ndt.etl.entity.monitor;
/**
 * 
 * @ClassName: AlarmLogs
 * @Description: 告警日志的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class AlarmLogs {
	
	private Integer id;  //id
	private Integer ruleId; //规则的ID
	private Integer alarmType; //告警的类型
	private String createTime;  //告警的时间
	private String description; //告警的描述
	private Integer status;     //告警的状态
	private String hostName;    //主机名
	private String ipAddress;   //主机的ip
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public Integer getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
