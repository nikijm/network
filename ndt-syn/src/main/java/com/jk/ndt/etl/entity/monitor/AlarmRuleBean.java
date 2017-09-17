package com.jk.ndt.etl.entity.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 规则管理的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AlarmRuleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;   //id
	private Integer serverId; //zhu主机id
	private Integer ruleId; //角色ID
	private String name;    //规则名称
	private Integer machineAndId; //机器
	private String monitorResource; //资源
	private String alarmEmail;  //告警邮件地址
	private String createUser;  //创建用户
	private String createTime;  //创建时间
	private Integer isUse;     //是否使用
	private Integer useCount;  //使用次数
	private Long  recentTrigger;  //最近触发时间
	private String  recentTriggerView;  //最近触发时间显示
	private Long  calculate;  
	private Integer triggerInterval; //1  半小时   2一小时  3 二小时 4 小时
	private Integer type;   //资源类型
	private String expression;  //表达式
	private Integer expressionType;  //1>= 2 <= 3 包含
	private String token;     //url的key
	private String content;  //规则的内容
	private List<Integer> machineView=new ArrayList<>();
	private Integer[] machineType;    
	private String description;      //主机 描述
	private String descriptionRule;  //规则的描述
	
	
	public String getDescriptionRule() {
		return descriptionRule;
	}
	public void setDescriptionRule(String descriptionRule) {
		this.descriptionRule = descriptionRule;
	}
	//主机名
	private String hostName;
	//ip地址
	private String ipAddress;
	
	
	public String getRecentTriggerView() {
		return recentTriggerView;
	}
	public void setRecentTriggerView(String recentTriggerView) {
		this.recentTriggerView = recentTriggerView;
	}
	public Long getRecentTrigger() {
		return recentTrigger;
	}
	public void setRecentTrigger(Long recentTrigger) {
		this.recentTrigger = recentTrigger;
	}
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer[] getMachineType() {
		return machineType;
	}
	public void setMachineType(Integer[] machineType) {
		this.machineType = machineType;
	}
	public List<Integer> getMachineView() {
		return machineView;
	}
	public void setMachineView(List<Integer> machineView) {
		this.machineView = machineView;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getMachineAndId() {
		return machineAndId;
	}
	public void setMachineAndId(Integer machineAndId) {
		this.machineAndId = machineAndId;
	}
	public String getMonitorResource() {
		return monitorResource;
	}
	public void setMonitorResource(String monitorResource) {
		this.monitorResource = monitorResource;
	}
	public String getAlarmEmail() {
		return alarmEmail;
	}
	public void setAlarmEmail(String alarmEmail) {
		this.alarmEmail = alarmEmail;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public Integer getUseCount() {
		return useCount;
	}
	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}
	
	
	public Long getCalculate() {
		return calculate;
	}
	public void setCalculate(Long calculate) {
		this.calculate = calculate;
	}
	public Integer getTriggerInterval() {
		return triggerInterval;
	}
	public void setTriggerInterval(Integer triggerInterval) {
		this.triggerInterval = triggerInterval;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public Integer getExpressionType() {
		return expressionType;
	}
	public void setExpressionType(Integer expressionType) {
		this.expressionType = expressionType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
