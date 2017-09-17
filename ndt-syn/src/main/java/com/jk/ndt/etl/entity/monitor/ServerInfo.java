package com.jk.ndt.etl.entity.monitor;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控信息的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ServerInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2930637548482465121L;
	
	@JSONField(serialize=false)
	private Integer id;   //表的ID
	@JSONField(name="server_id")
	private Integer serverId;//主机的ID
	@JSONField(name="cpu_usage")
	private Integer cpuUsage;//cpu的使用率
	@JSONField(name="memory_usage")
	private Integer memoryUsage;//内存的使用率
	@JSONField(name="disk_usage")
	private Integer diskUsage;//磁盘的使用率
	@JSONField(name="create_time")
    private String createTime;//创建的时间
    @JSONField(serialize=false)
    private String tablename;//表的名称
    private Long calculate;//时间搓
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public Integer getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(Integer cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public Integer getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(Integer memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public Integer getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(Integer diskUsage) {
		this.diskUsage = diskUsage;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public Long getCalculate() {
		return calculate;
	}
	public void setCalculate(Long calculate) {
		this.calculate = calculate;
	}
    
  
}
