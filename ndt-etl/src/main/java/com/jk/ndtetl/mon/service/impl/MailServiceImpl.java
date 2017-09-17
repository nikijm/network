package com.jk.ndtetl.mon.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.Constant;
import com.jk.ndtetl.mon.AlarmLogs;
import com.jk.ndtetl.mon.Email;
import com.jk.ndtetl.mon.dao.MailInfoDao;
import com.jk.ndtetl.mon.service.AlarmRuleService;
import com.jk.ndtetl.mon.service.MailService;
import com.jk.ndtetl.util.DateUtils;
import com.jk.ndtetl.util.monitor.VelocityUtil;

/**
 * 
 * @ClassName: MailServiceImpl
 * @Description: 发送邮件的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("mailService")
public class MailServiceImpl implements MailService {

    @Autowired
    MailInfoDao mailInfoDao;

    @Autowired
    AlarmRuleService alarmRuleService;

    public void sendAlarmMail(Email email) throws Exception {
        Properties prop = new Properties();
        prop.put("mail.host", email.getMailHost());
        prop.put("mail.transport.protocol", email.getProtocol());
        if (email.getIsAuth() != null && email.getIsAuth() == 1) {
            prop.put("mail.smtp.auth", true);
        }
        else {
            System.out.println("需要认证");
            return;
        }
        // 使用java发送邮件5步骤
        // 1.创建sesssion
        Session session = Session.getInstance(prop);
        // 开启session的调试模式，可以查看当前邮件发送状态
        session.setDebug(true);
        // 2.通过session获取Transport对象（发送邮件的核心API）
        Transport ts = session.getTransport();
        // 3.通过邮件用户名密码链接
        ts.connect(email.getFromAddress(), email.getPassword());
        // 4.创建邮件
        Message msg = createSimpleMail(session, email);
        // 5.发送电子邮件
        ts.sendMessage(msg, msg.getAllRecipients());
        ts.close();
    }

    /**
     * 业务发送邮件方法
     * 
     * @param email
     * @param alarmLogs
     */
    public void sendMail(Email email, AlarmLogs alarmLogs) {
        if (email == null) {
            return;
        }
        String title = email.getTitle() == null ? null : email.getTitle();
        String resource = email.getResource() == null ? null : email.getResource();
        String content = email.getContent() == null ? null : email.getContent();
        try {
            String content2 = VelocityUtil.getBusinessContent();
            Map<String, Object> map = VelocityUtil.getBusinessMap(title, resource, content);
            String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
            Email sendMailInfo = getSendMailInfo();
            sendMailInfo.setContent(loadTemplateContent);
            sendMailInfo.setTitle(title);
            sendMailInfo.setResource(resource);
            alarmLogs = getBusinessAlarmLog(alarmLogs);
            if (alarmLogs == null) {
                System.out.println("发送邮件失败");
                return;
            }
            alarmRuleService.insertBusinessAlarmLogs(alarmLogs);
            sendAlarmMail(sendMailInfo);
            // throw new RuntimeException();
        }
        catch (Exception e) {
            alarmLogs.setStatus(0);
            alarmRuleService.updateAlarmLogs(alarmLogs);
        }
    }

    private AlarmLogs getBusinessAlarmLog(AlarmLogs alarmLogs) {
        if (alarmLogs == null) {
            return null;
        }
        String dateToString = DateUtils.getDateToString(new Date());
        alarmLogs.setCreateTime(dateToString);
        alarmLogs.setStatus(1);
        alarmLogs.setType(Constant.ALARM_TYPE_BUSINESS);
        return alarmLogs;
    }

    public static MimeMessage createSimpleMail(Session session, Email email) throws Exception {
        // 创建邮件对象
        MimeMessage mm = new MimeMessage(session);
        // 设置发件人
        mm.setFrom(new InternetAddress(email.getFromAddress()));
        // 设置收件人
        mm.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getToAddress()));
        // 设置抄送人
        // mm.setRecipient(Message.RecipientType.CC, new
        // InternetAddress("用户名@163.com"));

        mm.setSubject(email.getSubject());
        mm.setContent(email.getContent(), "text/html;charset=utf-8");

        return mm;

    }

    @Override
    public Email getSendMailInfo() {
        Email sendMailInfo = mailInfoDao.getSendMailInfo();
        return sendMailInfo;
    }
}
