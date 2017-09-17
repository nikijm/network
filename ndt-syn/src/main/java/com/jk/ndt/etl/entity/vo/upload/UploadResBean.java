package com.jk.ndt.etl.entity.vo.upload;

import com.alibaba.fastjson.annotation.JSONField;

public class UploadResBean {
	@JSONField(name = "RequestType")
	private String requestType;

	@JSONField(name = "TransactionID")
	private String transactionID;

	@JSONField(name = "ErrorCode")
	private Integer errorCode;

	@JSONField(name = "ErrorMsg")
	private String errorMsg;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
