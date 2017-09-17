package com.jk.ndt.etl.entity.monitor;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 服务主机的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ServerHost implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;//表的ID
	private String hostname;//主机名
	private String ipaddress;//IP地址
	@JSONField(serialize=false)
	private String macaddress;//电脑的mac地址
	private Double cpusize;//cpu的大小
	private Integer memorysize;//内存的大小
	private Integer disksize;//磁盘的大小
	private String description;//主机的描述
	@JSONField(serialize=false)
	private String tablename;//监控表的表名
	private String username;//服务器的用户名
	private String password;//服务器的密码
	private String identification;//服务器的标志特征
	private Integer statu;//服务器使用的状态
	private String serverNote;//服务器的备注
	
	
	public String getServerNote() {
		return serverNote;
	}
	public void setServerNote(String serverNote) {
		this.serverNote = serverNote;
	}
	public Integer getStatu() {
		return statu;
	}
	public void setStatu(Integer statu) {
		this.statu = statu;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	
	public String getMacaddress() {
		return macaddress;
	}
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}
	
	public Integer getDisksize() {
		return disksize;
	}
	public void setDisksize(Integer disksize) {
		this.disksize = disksize;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getCpusize() {
		return cpusize;
	}
	public void setCpusize(Double cpusize) {
		this.cpusize = cpusize;
	}
	public Integer getMemorysize() {
		return memorysize;
	}
	public void setMemorysize(Integer memorysize) {
		this.memorysize = memorysize;
	}
	

}
