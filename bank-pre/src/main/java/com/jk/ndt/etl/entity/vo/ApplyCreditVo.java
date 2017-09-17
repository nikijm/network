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
	private String actionDescription;
	private String request;

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

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

}
