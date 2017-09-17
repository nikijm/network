package com.jk.ndtetl.mon.service;

import com.jk.ndtetl.mon.AlarmLogs;
import com.jk.ndtetl.mon.Email;

/**
 * 
 * @ClassName: MailService
 * @Description: 邮件的服务接口
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface MailService {

    /**
     * 发送有邮件
     * 
     * @param email
     * @throws Exception
     */
    public void sendAlarmMail(Email email) throws Exception;

    /**
     * 获取发送邮件的信息
     * 
     * @return
     */
    public Email getSendMailInfo();

    /**
     * 发送业务邮件的日志
     * 
     * @param email
     * @param alarmLogs
     */
    public void sendMail(Email email, AlarmLogs alarmLogs);

}
