package com.jk.ndt.etl.entity.vo;

import java.io.Serializable;

public class ApplyCreditVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long mBusinesssyncId;
	private Long recordId;
	private String operatorCode;
	private String operatorName;
	private String bPCode;
	private String bPName;
	private String address;
	private String applyUserID;
	private String applyUserName;
	private String applyUserPhone;
	private String financingMode;
	private String annualRate;
	private String investmentIndustry;
	private String projectType;
	private String amount;
	private String termMonth;
	private String corpusPayMethod;
	private String assetCodes;
	private String assetNames;
	private String projectDoc;
	private String actionDescription;

	public Long getmBusinesssyncId() {
		return mBusinesssyncId;
	}

	public void setmBusinesssyncId(Long mBusinesssyncId) {
		this.mBusinesssyncId = mBusinesssyncId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

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

	public String getApplyUserID() {
		return applyUserID;
	}

	public void setApplyUserID(String applyUserID) {
		this.applyUserID = applyUserID;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getApplyUserPhone() {
		return applyUserPhone;
	}

	public void setApplyUserPhone(String applyUserPhone) {
		this.applyUserPhone = applyUserPhone;
	}

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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTermMonth() {
		return termMonth;
	}

	public void setTermMonth(String termMonth) {
		this.termMonth = termMonth;
	}

	public String getCorpusPayMethod() {
		return corpusPayMethod;
	}

	public void setCorpusPayMethod(String corpusPayMethod) {
		this.corpusPayMethod = corpusPayMethod;
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
