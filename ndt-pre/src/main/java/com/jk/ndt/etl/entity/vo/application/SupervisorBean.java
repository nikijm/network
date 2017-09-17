package com.jk.ndt.etl.entity.vo.application;

import com.alibaba.fastjson.annotation.JSONField;

public class SupervisorBean {
	@JSONField(name = "ID")
	private String iD;

	@JSONField(name = "Name")
	private String name;

	@JSONField(name = "Phone")
	private String phone;

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
