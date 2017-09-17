package com.jk.ndt.etl.entity.vo.application;

import com.alibaba.fastjson.annotation.JSONField;

public class AttributeBean {
	@JSONField(name = "FinancingMode")
	private String financingMode;

	@JSONField(name = "AnnualRate")
	private String annualRate;

	@JSONField(name = "InvestmentIndustry")
	private String investmentIndustry;

	@JSONField(name = "ProjectType")
	private String projectType;

	@JSONField(name = "MinAmt")
	private Long minAmt;

	@JSONField(name = "MaxAmtl")
	private Long maxAmtl;

	@JSONField(name = "AssetCodes")
	private String assetCodes;

	@JSONField(name = "AssetNames")
	private String assetNames;

	@JSONField(name = "ProjectDoc")
	private String projectDoc;

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

	public String getInvestmentIndustry() {
		return investmentIndustry;
	}

	public void setInvestmentIndustry(String investmentIndustry) {
		this.investmentIndustry = investmentIndustry;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Long getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(Long minAmt) {
		this.minAmt = minAmt;
	}

	public Long getMaxAmtl() {
		return maxAmtl;
	}

	public void setMaxAmtl(Long maxAmtl) {
		this.maxAmtl = maxAmtl;
	}

	public String getAssetCodes() {
		return assetCodes;
	}

	public void setAssetCodes(String assetCodes) {
		this.assetCodes = assetCodes;
	}

	public String getAssetNames() {
		return assetNames;
	}

	public void setAssetNames(String assetNames) {
		this.assetNames = assetNames;
	}

	public String getProjectDoc() {
		return projectDoc;
	}

	public void setProjectDoc(String projectDoc) {
		this.projectDoc = projectDoc;
	}
	
	
}