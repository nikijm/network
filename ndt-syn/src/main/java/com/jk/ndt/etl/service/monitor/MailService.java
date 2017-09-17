package com.jk.ndt.etl.service.monitor;

import com.jk.ndt.etl.entity.monitor.Email;

public interface MailService {

	/**
	 * 发送有邮件
	 * 
	 * @param email
	 * @throws Exception
	 */
	public void SendAlarmMail(Email email) throws Exception;

	/**
	 * 获取发送邮件的信息
	 * 
	 * @return
	 */
	public Email getSendMailInfo();

}
