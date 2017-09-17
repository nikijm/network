package com.jk.ndtetl.util.monitor;

import java.util.Date;

import com.jk.ndtetl.mon.LogManager;


public class LogsUtil {
	
	public static LogManager getLogInfo(String description,String requestUri,String loginName,Integer level){
		LogManager logManager=new LogManager();
		logManager.setDescriptionLog(description);
		logManager.setDateTime(new Date().getTime());
		logManager.setLogLevel(level);
		logManager.setRequestUri(requestUri);
		logManager.setUserName(loginName);
		return logManager;
	}

}
