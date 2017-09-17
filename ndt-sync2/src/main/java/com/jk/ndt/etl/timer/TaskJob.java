package com.jk.ndt.etl.timer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.dao.monitor.ServerInfoDao;
import com.jk.ndt.etl.entity.monitor.AlarmRuleBean;
import com.jk.ndt.etl.entity.monitor.ServerHost;
import com.jk.ndt.etl.entity.monitor.ServerInfo;
import com.jk.ndt.etl.service.monitor.AlarmRuleService;
import com.jk.ndt.etl.service.monitor.MailService;
import com.jk.ndt.etl.service.monitor.MonitorService;
import com.jk.ndt.etl.service.monitor.ServerHostService;
import com.jk.ndt.etl.service.monitor.ServerInfoService;
import com.jk.ndt.etl.utility.DesUtils;

public class TaskJob{

	@Autowired
	ServerHostService serverHostService;

	@Autowired
	ServerInfoService serverInfoService;

	@Autowired
	ServerInfoDao serverInfoDao;
	
	@Autowired
	AlarmRuleService alarmRuleService;
	
	@Autowired
	MonitorService monitorService;
	@Autowired
	MailService mailService;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 * 执行监控cpu,内存占用率,磁盘使用情况
	 */
	@Scheduled(cron = "0 */15 * * * ?")
	public void excuteMonitor() {
		System.out.println("执行开始");
		try {
		    List<ServerHost> serverBeanList=serverHostService.listServerhostInfo();
		    for (ServerHost serverHost : serverBeanList) {
		        serverHost.setPassword(DesUtils.decoded(serverHost.getPassword(), Constant.encodeKey));
		        ServerInfo serverInfo = monitorService.getinfo(serverHost.getIpaddress(), serverHost.getUsername(), serverHost.getPassword());
		        serverInfo.setTablename(serverHost.getTablename());
		        serverInfoService.insertServerInfo(serverInfo);
		    }
		    System.out.println("执行结束");
        } catch (Exception e) {
        	logger.error("获取数据失败");
            e.printStackTrace();
        }
	}
	
	@Scheduled(cron = "0 */1 * * * ?")
	public void alarmMonitor() {
		System.out.println("执行开始");
		try {
		    List<ServerHost> serverBeanList=serverHostService.listServerhostInfo();
		    for (ServerHost serverHost : serverBeanList) {
		        serverHost.setPassword(DesUtils.decoded(serverHost.getPassword(), Constant.encodeKey));
		        ServerInfo serverInfo = monitorService.getinfo(serverHost.getIpaddress(), serverHost.getUsername(), serverHost.getPassword());
		        List<AlarmRuleBean> listAlarmIdByServerId = alarmRuleService.listAlarmIdByServerId(serverHost.getId());
		       if(listAlarmIdByServerId!=null&&listAlarmIdByServerId.size()>0){
		    	   for (AlarmRuleBean alarmRuleBean : listAlarmIdByServerId) {
		    		   if(alarmRuleBean!=null){
		    			   if(alarmRuleBean.getType()==Constant.HOSTNAME_TYPE_CPU){
		    				   Integer cpuUsage = serverInfo.getCpuUsage();
		    				   monitorService.alarmRuleCpu(serverHost,cpuUsage,alarmRuleBean);
		    			   }else if(alarmRuleBean.getType()==Constant.HOSTNAME_TYPE_MEMORY){
		    				   Integer memoryUsage = serverInfo.getMemoryUsage();
		    				   monitorService.alarmRuleMemory(serverHost,memoryUsage,alarmRuleBean);
		    			   }else if(alarmRuleBean.getType()==Constant.HOSTNAME_TYPE_DISK){
		    				   Integer diskUsage = serverInfo.getDiskUsage();
		    				   monitorService.alarmRuleDisk(serverHost,diskUsage,alarmRuleBean);
		    			   }else if(alarmRuleBean.getType()==Constant.HOSTNAME_TYPE_LOG){
		    				   monitorService.alarmRuleLogs(serverHost,alarmRuleBean);
		    			   }
		    		   }else{
		    			   System.out.println("ruleid不存在或者不可用");
		    			   logger.error("ruleid不存在或者不可用");
		    		   }
		    	   }
		       }
		    }
		    System.out.println("执行结束");
        } catch (Exception e) {
        	logger.error("获取数据失败");
            e.printStackTrace();
        }
	}
	
//	@Scheduled(cron = "0 */3 * * * ?")
//	public void repeatSendAlarmMail(){
//		try {
//			Email sendMailInfo = mailService.getSendMailInfo();
//			List<AlarmLogs> alarmRuleList=alarmRuleService.getRepeatSendAlarmMailList();
//			ServerHost serverHost=null;
//			if(alarmRuleList!=null&&alarmRuleList.size()>0){
//				for (AlarmLogs alarmLogs : alarmRuleList) {
//					serverHost=new ServerHost();
//					serverHost.setDescription(alarmLogs.getHostName());
//					serverHost.setIpaddress(alarmLogs.getIpAddress());
//					AlarmRuleBean alarmRule =alarmRuleService.getAlarmRuleById(alarmLogs.getRuleId());
//					if(alarmRule!=null){
//						sendMailInfo.setToAddress(alarmRule.getAlarmEmail());
//						if(alarmRule.getType()==Constant.HOSTNAME_TYPE_CPU){
//							String content2 = VelocityUtil.getContent();
//							Map<String, Object> map = VelocityUtil.getMap("cpu告警通知", "cpu", serverHost.getHostname(), serverHost.getIpaddress(), "cpu占用率超过"+alarmRule.getContent()+"，请及时处理");
//							String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
//							sendMailInfo.setSubject((String)map.get("title"));
//							sendMailInfo.setContent(loadTemplateContent);
//							String content="cpu告警";
//							monitorService.repeatEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
//							alarmLogs.setStatus(1);
//							alarmRuleService.updateAlarmLogs(alarmLogs);
//						}else if(alarmRule.getType()==Constant.HOSTNAME_TYPE_MEMORY){
//							String content2 = VelocityUtil.getContent();
//							Map<String, Object> map = VelocityUtil.getMap("内存告警通知", "内存", serverHost.getHostname(), serverHost.getIpaddress(), "内存占用率超过"+alarmRule.getContent()+"，请及时处理");
//							String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
//							sendMailInfo.setSubject((String)map.get("title"));
//							sendMailInfo.setContent(loadTemplateContent);
//							String content="内存告警";
//							monitorService.repeatEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
//							alarmLogs.setStatus(1);
//							alarmRuleService.updateAlarmLogs(alarmLogs);
//						}else if(alarmRule.getType()==Constant.HOSTNAME_TYPE_DISK){
//							String content2 = VelocityUtil.getContent();
//							Map<String, Object> map = VelocityUtil.getMap("磁盘告警通知", "磁盘", serverHost.getHostname(), serverHost.getIpaddress(), "磁盘使用率超过"+alarmRule.getContent()+"，请及时处理");
//							String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
//							sendMailInfo.setSubject((String)map.get("title"));
//							sendMailInfo.setContent(loadTemplateContent);
//							String content="磁盘告警";
//							monitorService.repeatEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
//							alarmLogs.setStatus(1);
//							alarmRuleService.updateAlarmLogs(alarmLogs);
//						}else if(alarmRule.getType()==Constant.HOSTNAME_TYPE_LOG){
//							String content2 = VelocityUtil.getContent();
//							Map<String, Object> map = VelocityUtil.getMap("日志告警通知", "磁盘", serverHost.getHostname(), serverHost.getIpaddress(), "磁盘使用率超过"+alarmRule.getContent()+"，请及时处理");
//							String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
//							sendMailInfo.setSubject((String)map.get("title"));
//							sendMailInfo.setContent(loadTemplateContent);
//							String content="日志告警";
//							monitorService.repeatEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
//							alarmLogs.setStatus(1);
//							alarmRuleService.updateAlarmLogs(alarmLogs);
//						}
//					}
//				}
//			 }
//		} catch (Exception e) {
//				System.out.println("发送邮件失败");
//		}
//	}
	
}