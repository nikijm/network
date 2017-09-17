package com.jk.ndt.etl.dao.monitor;

import com.jk.ndt.etl.entity.monitor.Email;

public interface MailInfoDao {

	/**
	 * 获取发送邮件的信息
	 * @return
	 */
	public Email getSendMailInfo();

}
