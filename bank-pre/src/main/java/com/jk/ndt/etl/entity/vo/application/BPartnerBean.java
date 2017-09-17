package com.jk.ndt.etl.entity.vo.application;

import com.alibaba.fastjson.annotation.JSONField;

public class BPartnerBean {
	@JSONField(name = "BPCode")
	private String bPCode;

	@JSONField(name = "BPName")
	private String bPName;

	@JSONField(name = "Address")
	private String address;

	@JSONField(name = "Attn")
	private AttnBean attn;

	@JSONField(name = "Supervisor")
	private SupervisorBean supervisor;

	@JSONField(name = "Legal")
	private LegalBean legal;

	public String getbPCode() {
		return bPCode;
	}

	public void setbPCode(String bPCode) {
		this.bPCode = bPCode;
	}

	public String getbPName() {
		return bPName;
	}

	public void setbPName(String bPName) {
		this.bPName = bPName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public AttnBean getAttn() {
		return attn;
	}

	public void setAttn(AttnBean attn) {
		this.attn = attn;
	}

	public SupervisorBean getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(SupervisorBean supervisor) {
		this.supervisor = supervisor;
	}

	public LegalBean getLegal() {
		return legal;
	}

	public void setLegal(LegalBean legal) {
		this.legal = legal;
	}

}
