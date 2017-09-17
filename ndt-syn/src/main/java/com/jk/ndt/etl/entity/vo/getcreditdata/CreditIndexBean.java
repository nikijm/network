package com.jk.ndt.etl.entity.vo.getcreditdata;

import com.alibaba.fastjson.annotation.JSONField;

public class CreditIndexBean {
	@JSONField(name = "BasicInfor")
	private String basicInfor;

	@JSONField(name = "OperatingInfor")
	private String operatingInfor;

	@JSONField(name = "WarrantInfor")
	private String warrantInfor;

	@JSONField(name = "FinancialInfor")
	private String financialInfor;

	@JSONField(name = "InsuranceInfor")
	private String insuranceInfor;

	@JSONField(name = "Honor")
	private String honor;

	@JSONField(name = "Penalties")
	private String penalties;

	@JSONField(name = "PaymentInfor")
	private String paymentInfor;

	@JSONField(name = "TradingInfor")
	private String tradingInfor;

	@JSONField(name = "PersonalInfor")
	private String personalInfor;

	public String getBasicInfor() {
		return basicInfor;
	}

	public void setBasicInfor(String basicInfor) {
		this.basicInfor = basicInfor;
	}

	public String getOperatingInfor() {
		return operatingInfor;
	}

	public void setOperatingInfor(String operatingInfor) {
		this.operatingInfor = operatingInfor;
	}

	public String getWarrantInfor() {
		return warrantInfor;
	}

	public void setWarrantInfor(String warrantInfor) {
		this.warrantInfor = warrantInfor;
	}

	public String getFinancialInfor() {
		return financialInfor;
	}

	public void setFinancialInfor(String financialInfor) {
		this.financialInfor = financialInfor;
	}

	public String getInsuranceInfor() {
		return insuranceInfor;
	}

	public void setInsuranceInfor(String insuranceInfor) {
		this.insuranceInfor = insuranceInfor;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public String getPenalties() {
		return penalties;
	}

	public void setPenalties(String penalties) {
		this.penalties = penalties;
	}

	public String getPaymentInfor() {
		return paymentInfor;
	}

	public void setPaymentInfor(String paymentInfor) {
		this.paymentInfor = paymentInfor;
	}

	public String getTradingInfor() {
		return tradingInfor;
	}

	public void setTradingInfor(String tradingInfor) {
		this.tradingInfor = tradingInfor;
	}

	public String getPersonalInfor() {
		return personalInfor;
	}

	public void setPersonalInfor(String personalInfor) {
		this.personalInfor = personalInfor;
	}
	
	
}
