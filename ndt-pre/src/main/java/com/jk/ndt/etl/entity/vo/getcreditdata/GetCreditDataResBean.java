package com.jk.ndt.etl.entity.vo.getcreditdata;

import com.alibaba.fastjson.annotation.JSONField;

public class GetCreditDataResBean {

	@JSONField(name = "RequestType")
	private String requestType;

	@JSONField(name = "TransactionID")
	private String transactionID;

	@JSONField(name = "ErrorCode")
	private Integer errorCode;

	@JSONField(name = "ErroeMsg")
	private String erroeMsg;

	@JSONField(name = "Data")
	private DataBean data;

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

	public String getErroeMsg() {
		return erroeMsg;
	}

	public void setErroeMsg(String erroeMsg) {
		this.erroeMsg = erroeMsg;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

}
