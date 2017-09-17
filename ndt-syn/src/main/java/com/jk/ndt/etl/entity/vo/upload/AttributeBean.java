package com.jk.ndt.etl.entity.vo.upload;

import com.alibaba.fastjson.annotation.JSONField;

public class AttributeBean {
	@JSONField(name = "FinancingMode")
	private String financingMode;

	@JSONField(name = "AnnualRate")
	private String annualRate;

	@JSONField(name = "IndustryCodes")
	private String industryCodes;

	@JSONField(name = "IndustryNames")
	private String industryNames;

	@JSONField(name = "SuitableObjs")
	private String suitableObjs;

	@JSONField(name = "ProjectTypeCodes")
	private String projectTypeCodes;

	@JSONField(name = "ProjectTypeNames")
	private String projectTypeNames;

	@JSONField(name = "MinAmt")
	private Long minAmt;

	@JSONField(name = "MaxAmt")
	private Long maxAmt;

	@JSONField(name = "InvestmentCycle")
	private String investmentCycle;

	@JSONField(name = "IRCR")
	private String iRCR;

	@JSONField(name = "AssetTypeCodes")
	private String assetTypeCodes;

	@JSONField(name = "AssetTypeNames")
	private String assetTypeNames;

	@JSONField(name = "WithdrawMode")
	private String withdrawMode;

	@JSONField(name = "ProjectDocRequired")
	private String projectDocRequired;

	@JSONField(name = "Remark")
	private String remark;

	public String getFinancingMode() {
		return financingMode;
	}

	public void setFinancingMode(String financingMode) {
		this.financingMode = financingMode;
	}

	public String getAnnualRate() {
		return annualRate;
	}

	public void setAnnualRate(String annualRate) {
		this.annualRate = annualRate;
	}

	public String getIndustryCodes() {
		return industryCodes;
	}

	public void setIndustryCodes(String industryCodes) {
		this.industryCodes = industryCodes;
	}

	public String getIndustryNames() {
		return industryNames;
	}

	public void setIndustryNames(String industryNames) {
		this.industryNames = industryNames;
	}

	public String getSuitableObjs() {
		return suitableObjs;
	}

	public void setSuitableObjs(String suitableObjs) {
		this.suitableObjs = suitableObjs;
	}

	public String getProjectTypeCodes() {
		return projectTypeCodes;
	}

	public void setProjectTypeCodes(String projectTypeCodes) {
		this.projectTypeCodes = projectTypeCodes;
	}

	public String getProjectTypeNames() {
		return projectTypeNames;
	}

	public void setProjectTypeNames(String projectTypeNames) {
		this.projectTypeNames = projectTypeNames;
	}

	public Long getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(Long minAmt) {
		this.minAmt = minAmt;
	}

	public Long getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(Long maxAmt) {
		this.maxAmt = maxAmt;
	}

	public String getInvestmentCycle() {
		return investmentCycle;
	}

	public void setInvestmentCycle(String investmentCycle) {
		this.investmentCycle = investmentCycle;
	}

	public String getiRCR() {
		return iRCR;
	}

	public void setiRCR(String iRCR) {
		this.iRCR = iRCR;
	}

	public String getAssetTypeCodes() {
		return assetTypeCodes;
	}

	public void setAssetTypeCodes(String assetTypeCodes) {
		this.assetTypeCodes = assetTypeCodes;
	}

	public String getAssetTypeNames() {
		return assetTypeNames;
	}

	public void setAssetTypeNames(String assetTypeNames) {
		this.assetTypeNames = assetTypeNames;
	}

	public String getWithdrawMode() {
		return withdrawMode;
	}

	public void setWithdrawMode(String withdrawMode) {
		this.withdrawMode = withdrawMode;
	}

	public String getProjectDocRequired() {
		return projectDocRequired;
	}

	public void setProjectDocRequired(String projectDocRequired) {
		this.projectDocRequired = projectDocRequired;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
