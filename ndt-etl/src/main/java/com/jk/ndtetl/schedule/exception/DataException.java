package com.jk.ndtetl.schedule.exception;

public class DataException extends TaskException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataException(String errorCode, String errorDesc) {
		super(errorCode, errorDesc);
	}

}
