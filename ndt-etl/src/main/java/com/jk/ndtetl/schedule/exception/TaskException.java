package com.jk.ndtetl.schedule.exception;

public class TaskException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode = null;
	private String errorDesc = null;
	public TaskException(String errorCode, String errorDesc) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String errorDesc(){
		StringBuilder sb = new StringBuilder();
		sb.append(errorCode);
		sb.append("\n");
		sb.append(errorDesc);
		return sb.toString();
	}
}
