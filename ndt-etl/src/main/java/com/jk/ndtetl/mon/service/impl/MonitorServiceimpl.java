package com.jk.ndtetl.mon.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.Constant;
import com.jk.ndtetl.mon.AlarmLogs;
import com.jk.ndtetl.mon.AlarmRuleBean;
import com.jk.ndtetl.mon.ClearMemoryInfo;
import com.jk.ndtetl.mon.DiskUseInfo;
import com.jk.ndtetl.mon.Email;
import com.jk.ndtetl.mon.HostConfigurationInfo;
import com.jk.ndtetl.mon.MemoryClear;
import com.jk.ndtetl.mon.ServerHost;
import com.jk.ndtetl.mon.ServerInfo;
import com.jk.ndtetl.mon.ServerInfoQuery;
import com.jk.ndtetl.mon.SysCpuInfo;
import com.jk.ndtetl.mon.SysMemInfo;
import com.jk.ndtetl.mon.dao.ServerInfoDao;
import com.jk.ndtetl.mon.service.AlarmRuleService;
import com.jk.ndtetl.mon.service.MailService;
import com.jk.ndtetl.mon.service.MonitorService;
import com.jk.ndtetl.mon.service.ServerHostService;
import com.jk.ndtetl.mon.service.ServerInfoService;
import com.jk.ndtetl.util.DateUtils;
import com.jk.ndtetl.util.DesUtils;
import com.jk.ndtetl.util.monitor.VelocityUtil;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控的service
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("monitorService")
public class MonitorServiceimpl implements MonitorService {

    @Autowired
    ServerHostService serverHostService;

    @Autowired
    ServerInfoService serverInfoService;

    @Autowired
    ServerInfoDao serverInfoDao;

    @Autowired
    AlarmRuleService alarmRuleService;

    @Autowired
    MailService mailService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void alarmRuleLogs(ServerHost serverHost, AlarmRuleBean alarmRule) {
        String str = null;
        String expression = alarmRule.getExpression();
        if (expression.contains("p")) {
            str = expression.split("p")[1];
        }
        if ("".contains(str)) {
            String content2 = VelocityUtil.getContent();
            Map<String, Object> map = VelocityUtil.getMap("日志告警通知", "磁盘", serverHost.getHostname(),
                    serverHost.getIpaddress(), "磁盘使用率超过" + alarmRule.getContent() + "，请及时处理");
            String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
            Email sendMailInfo = mailService.getSendMailInfo();
            sendMailInfo.setSubject((String) map.get("title"));
            sendMailInfo.setContent(loadTemplateContent);
            String content = "日志异常";
            simpleEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
            // logger.info("发邮件");
            logger.info("插入日志");
        }
    }

    public void alarmRuleDisk(ServerHost serverHost, Integer diskUsage, AlarmRuleBean alarmRule) {
        String str = null;
        String expression = alarmRule.getExpression();
        if (expression.contains("lt;")) {
            String[] split = expression.split("lt;");
            str = split[1];
        }
        Integer integer = Integer.valueOf(str);
        if (diskUsage > integer) {
            logger.info("发邮件");
            String content2 = VelocityUtil.getContent();
            Map<String, Object> map = VelocityUtil.getMap("磁盘告警通知", "磁盘", serverHost.getDescription(),
                    serverHost.getIpaddress(), "磁盘使用率超过" + integer + "，请及时处理");
            String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
            Email sendMailInfo = mailService.getSendMailInfo();
            sendMailInfo.setSubject((String) map.get("title"));
            sendMailInfo.setContent(loadTemplateContent);
            sendMailInfo.setToAddress(alarmRule.getAlarmEmail());
            String content = "磁盘异常";
            simpleEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
            logger.info("插入日志");
        }
        else {
            logger.info("不用管");
        }
    }

    public void alarmRuleMemory(ServerHost serverHost, Integer memoryUsage, AlarmRuleBean alarmRule) {

        String str = null;
        String expression = alarmRule.getExpression();
        if (expression.contains("lt;")) {
            str = expression.split("lt;")[1];
        }
        Integer integer = Integer.valueOf(str);
        if (memoryUsage > integer) {
            logger.info("发邮件");
            String content2 = VelocityUtil.getContent();
            Map<String, Object> map = VelocityUtil.getMap("内存告警通知", "内存", serverHost.getDescription(),
                    serverHost.getIpaddress(), "内存占用率超过" + integer + "，请及时处理");
            String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
            Email sendMailInfo = mailService.getSendMailInfo();
            sendMailInfo.setSubject((String) map.get("title"));
            sendMailInfo.setContent(loadTemplateContent);
            sendMailInfo.setToAddress(alarmRule.getAlarmEmail());
            String content = "内存异常";
            simpleEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
            logger.info("插入日志");
        }
        else {
            logger.info("不用管");
        }
    }

    public void simpleEmailAlarm(ServerHost serverHost, AlarmRuleBean alarmRule, Email sendMailInfo, String content) {
        Integer triggerInterval = alarmRule.getTriggerInterval();
        Long valueOf = Long.valueOf(triggerInterval);
        if (valueOf == 1) {
            valueOf = 30 * 60 * 1000L;
        }
        else if (valueOf == 2) {
            valueOf = 60 * 60 * 1000L;
        }
        else if (valueOf == 3) {
            valueOf = 2 * 60 * 60 * 1000L;
        }
        else if (valueOf == 4) {
            valueOf = 3 * 60 * 60 * 1000L;
        }
        Long recentTrigger = alarmRule.getRecentTrigger();
        Date date = new Date();
        Long currentTime = date.getTime();
        if (recentTrigger != null) {
            try {
                boolean flag = ((currentTime - valueOf) > recentTrigger) ? true : false;
                if (flag) {
                    sendEmail(serverHost, alarmRule, sendMailInfo, content);
                }
            }
            catch (Exception e) {
                System.out.println("时间转字符串失败");
                e.printStackTrace();
            }
        }
        else {
            sendEmail(serverHost, alarmRule, sendMailInfo, content);
        }
    }

    private void sendEmail(ServerHost serverHost, AlarmRuleBean alarmRule, Email sendMailInfo, String content) {
        try {
            AlarmLogs alarmLogs = null;
            try {
                alarmLogs = new AlarmLogs();
                insertAlarmLog(serverHost, alarmRule, content, alarmLogs);
                mailService.sendAlarmMail(sendMailInfo);
                // throw new RuntimeException();
            }
            catch (Exception e) {
                logger.info("发送邮件失败");
                alarmLogs.setStatus(0);
                alarmRuleService.updateAlarmLogs(alarmLogs);
            }
            // String dateToString = DateUtils.getDateToString(new
            // Date(currentTime));
            alarmRule.setRecentTrigger(new Date().getTime());
            Integer useCount = alarmRule.getUseCount();
            if (useCount == null) {
                useCount = 1;
            }
            else {
                useCount = useCount + 1;
            }
            alarmRule.setUseCount(useCount);
            alarmRuleService.updateAlarmRule(alarmRule);
        }
        catch (Exception e) {
            System.out.println("发送邮件失败");
            e.printStackTrace();
        }
    }

    public void repeatEmailAlarm(ServerHost serverHost, AlarmRuleBean alarmRule, Email sendMailInfo, String content) {
        Integer triggerInterval = alarmRule.getTriggerInterval();
        Long valueOf = Long.valueOf(triggerInterval);
        if (valueOf == 1) {
            valueOf = 30 * 60 * 1000L;
        }
        else if (valueOf == 2) {
            valueOf = 60 * 60 * 1000L;
        }
        else if (valueOf == 3) {
            valueOf = 2 * 60 * 60 * 1000L;
        }
        else if (valueOf == 4) {
            valueOf = 3 * 60 * 60 * 1000L;
        }
        Long recentTrigger = alarmRule.getRecentTrigger();
        Date date = new Date();
        Long currentTime = date.getTime();
        if (recentTrigger != null) {
            try {
                boolean flag = ((currentTime - valueOf) > recentTrigger) ? true : false;
                if (flag) {
                    repeatSendMail(alarmRule, sendMailInfo);
                }
            }
            catch (Exception e) {
                System.out.println("时间转字符串失败");
                e.printStackTrace();
            }

        }
        else {
            repeatSendMail(alarmRule, sendMailInfo);
        }
    }

    private void repeatSendMail(AlarmRuleBean alarmRule, Email sendMailInfo) {
        try {
            AlarmLogs alarmLogs = null;
            try {
                mailService.sendAlarmMail(sendMailInfo);
                alarmLogs = new AlarmLogs();
                alarmLogs.setStatus(1);
                alarmRuleService.updateAlarmLogs(alarmLogs);
                // throw new RuntimeException();
            }
            catch (Exception e) {
                logger.info("发送邮件失败");
                alarmLogs.setStatus(0);
                alarmRuleService.updateAlarmLogs(alarmLogs);
            }
            // String dateToString = DateUtils.getDateToString(new
            // Date(currentTime));
            alarmRule.setRecentTrigger(new Date().getTime());
            Integer useCount = alarmRule.getUseCount();
            if (useCount == null) {
                useCount = 1;
            }
            else {
                useCount = useCount + 1;
            }
            alarmRule.setUseCount(useCount);
            alarmRuleService.updateAlarmRule(alarmRule);
        }
        catch (Exception e) {
            System.out.println("发送邮件失败");
            e.printStackTrace();
        }
    }

    private void insertAlarmLog(ServerHost serverHost, AlarmRuleBean alarmRule, String content, AlarmLogs alarmLogs) {

        String dateToString = DateUtils.getDateToString(new Date());
        alarmLogs.setCreateTime(dateToString);
        alarmLogs.setStatus(1);
        alarmLogs.setRuleId(alarmRule.getId());
        alarmLogs.setAlarmType(alarmRule.getType());
        alarmLogs.setDescription(content);
        alarmLogs.setHostName(serverHost.getDescription());
        alarmLogs.setIpAddress(serverHost.getIpaddress());
        alarmLogs.setType(Constant.ALARM_TYPE_MONITOR);
        alarmRuleService.insertAlarmLogs(alarmLogs);
    }

    public void alarmRuleCpu(ServerHost serverHost, Integer cpuUsage, AlarmRuleBean alarmRule) {
        String str = null;
        String expression = alarmRule.getExpression();
        if (expression.contains("lt;")) {
            String[] split = expression.split("lt;");
            str = split[1];
        }
        if (str == null) {
            System.out.println("表达式有误");
            return;
        }
        Integer integer = Integer.valueOf(str);
        if (cpuUsage > integer) {
            logger.info("发邮件");
            // 定义邮件的模板
            String content2 = VelocityUtil.getContent();
            Map<String, Object> map = VelocityUtil.getMap("cpu告警通知test", "cpu", serverHost.getDescription(),
                    serverHost.getIpaddress(), "cpu占用率超过" + integer + "，请及时处理");
            String loadTemplateContent = VelocityUtil.loadTemplateContent(content2, map);
            Email sendMailInfo = mailService.getSendMailInfo();
            if (sendMailInfo != null) {
                sendMailInfo.setSubject((String) map.get("title"));
                sendMailInfo.setContent(loadTemplateContent);
                sendMailInfo.setToAddress(alarmRule.getAlarmEmail());
                String content = "cpu异常";
                simpleEmailAlarm(serverHost, alarmRule, sendMailInfo, content);
            }
            logger.info("插入日志");
        }
        else {
            logger.info("cpu正常");
        }
    }

    public ServerInfoQuery queryFifteenDay() {
        ServerInfoQuery serverInfoQuery = new ServerInfoQuery();
        long start = (new Date().getTime()) - 15 * 24 * 60 * 60 * 1000;
        serverInfoQuery.setStartTime(start);
        serverInfoQuery.setEndTime(new Date().getTime());
        return serverInfoQuery;
    }

    public ServerInfoQuery querySevenDay() {
        ServerInfoQuery serverInfoQuery = new ServerInfoQuery();
        long start = (new Date().getTime()) - 7 * 24 * 60 * 60 * 1000;
        serverInfoQuery.setStartTime(start);
        serverInfoQuery.setEndTime(new Date().getTime());
        return serverInfoQuery;
    }

    public ServerInfoQuery queryOneDay() {
        ServerInfoQuery serverInfoQuery = new ServerInfoQuery();
        long start = (new Date().getTime()) - 1 * 24 * 60 * 60 * 1000;
        serverInfoQuery.setStartTime(start);
        serverInfoQuery.setEndTime(new Date().getTime());
        return serverInfoQuery;
    }

    public ServerInfoQuery queryOneHour() {
        ServerInfoQuery serverInfoQuery = new ServerInfoQuery();
        long start = (new Date().getTime()) - 1 * 60 * 60 * 1000;
        serverInfoQuery.setStartTime(start);
        serverInfoQuery.setEndTime(new Date().getTime());
        return serverInfoQuery;
    }

    /**
     * -----------------------------------------------
     * 阅读：朱生
     * 功效：获取linux服务器信息new并返回serverInfo对象
     * ？：存储了两种格式的时间值
     * @param ipAddress ip地址
     * @param username  用户名
     * @param password  解密后的密码
     * -----------------------------------------------
     * @return
     */
    public ServerInfo getInfo(String ipAddress, String username, String password) {
        ServerInfo serverInfo = getLinuxInfo(ipAddress, username, password);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        serverInfo.setCreateTime(sdf.format(date));
        serverInfo.setCalculate(date.getTime());
        return serverInfo;
    }

    public ServerHost getConfigurationInfo(String ipAddress, String username, String password) {
        ServerHost serverInfo = getLinuxConfigurationInfo(ipAddress, username, password);
        return serverInfo;
    }

    /**
     * 获取linux服务器信息
     * 
     * @param ipAddress
     * @param username
     * @param password
     * @return
     */
    public ServerInfo getLinuxInfo(String ipAddress, String username, String password) {

        ServerHost serverBean = null;
        // if (ipAddress == null) {
        // System.out.println("IP地址不能为空");
        // } else {
        // serverBean = serverHostService.getServerHostListByIp(ipAddress);
        // }
        ServerInfo serverInfo = new ServerInfo();
        Connection conn = null;
        boolean isAuthenticated = false;
        try {
            conn = new Connection(ipAddress); // hostname
            conn.connect();
            isAuthenticated = conn.authenticateWithPassword(username, password); // 你要远程登录的主机的用户名及密码,如admin/123456
            if (isAuthenticated == false) {
                System.out.println("SSH Login Authentication failed.");
                return null;
            }
            else {
                Session sess = conn.openSession();
                int cpuInfo = new SysCpuInfo(sess).getCPUInfo();
                serverInfo.setCpuUsage(cpuInfo);
                sess.close();
                sess = conn.openSession();
                // if (serverBean != null) {
                int memInfo = new SysMemInfo(sess).getMEMInfo();
                serverInfo.setMemoryUsage(memInfo);
                // }
                sess.close();
                sess = conn.openSession();
                int diskInfo = new DiskUseInfo(sess).getDiskInfo();
                serverInfo.setDiskUsage(diskInfo);
                // if (serverBean != null) {
                // serverInfo.setServerId(serverBean.getId());
                // } else {
                // System.out.println("serverBean 不能为空");
                // }
                sess.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return serverInfo;
    }

    /**
     * 获取linux服务器配置信息
     * 
     * @param ipAddress
     * @param username
     * @param password
     * @return
     */
    public ServerHost getLinuxConfigurationInfo(String ipAddress, String username, String password) {
        ServerHost serverHost = new ServerHost();
        Connection conn = null;
        boolean isAuthenticated = false;
        try {
            conn = new Connection(ipAddress); // hostname
            conn.connect();
            isAuthenticated = conn.authenticateWithPassword(username, password); // 你要远程登录的主机的用户名及密码,如admin/123456
            if (isAuthenticated == false) {
                System.out.println("SSH Login Authentication failed.");
                return null;
            }
            else {
                Session sess = conn.openSession();
                Integer cpuSize = new HostConfigurationInfo().getCpuSize(sess);
                serverHost.setCpusize(cpuSize);
                sess.close();
                sess = conn.openSession();
                Double memorySize = new HostConfigurationInfo().getMemorySize(sess);
                serverHost.setMemorysize(memorySize);
                sess.close();
                sess = conn.openSession();
                Double diskSize = new HostConfigurationInfo().getDiskSize(sess);
                serverHost.setDisksize(diskSize);
                sess.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return serverHost;
    }

    public Long selectServerInfoCount(String classification, String tablename) {
        if (classification.equals(Constant.MONITORY_INFO_HOUR)) {
            ServerInfoQuery queryOneHour = queryOneHour();
            queryOneHour.setTablename(tablename);
            long selectWebInfoCount = serverInfoDao.selectServerInfoCount(queryOneHour);
            return selectWebInfoCount;
        }
        else if (classification.equals(Constant.MONITORY_INFO_DAY)) {
            ServerInfoQuery queryOneDay = queryOneDay();
            queryOneDay.setTablename(tablename);
            long selectWebInfoCount = serverInfoDao.selectServerInfoCount(queryOneDay);
            return selectWebInfoCount;
        }
        else if (classification.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
            ServerInfoQuery serverInfoQuery = querySevenDay();
            serverInfoQuery.setTablename(tablename);
            long selectWebInfoCount = serverInfoDao.selectServerInfoCount(serverInfoQuery);
            return selectWebInfoCount;
        }
        else if (classification.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
            ServerInfoQuery serverInfoQuery = querySevenDay();
            serverInfoQuery.setTablename(tablename);
            long selectWebInfoCount = serverInfoDao.selectServerInfoCount(serverInfoQuery);
            return selectWebInfoCount;
        }
        return null;
    }

    /**
     * 阅读：朱生
     * 查询解析:sql为了排序查询结果中重复的时间值，采用了相同时间则获取最新的一条
     * 时间：2017年7月27日 11:27:32
     * --------------------------------------------------------------
     * @param tablename
     * @param monitorCompunent
     * @param dateTime
     * @return
     */
    @Override
    public List<Long> getMonitorInfo(String tablename, String monitorCompunent, String dateTime) {

        if (monitorCompunent.equals(Constant.RESOURCE_TYPE_CPU)) {
            if (dateTime.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorCpuInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorCpuInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorCpuInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfoWeb = serverInfoDao.getMonitorCpuInfo(serverInfoQuery);
                return serverInfoWeb;
            }
        }
        else if (monitorCompunent.equals(Constant.RESOURCE_TYPE_MEMORY)) {
            if (dateTime.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorMemoryInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorMemoryInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorMemoryInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfoWeb = serverInfoDao.getMonitorMemoryInfo(serverInfoQuery);
                return serverInfoWeb;
            }
        }
        else if (monitorCompunent.equals(Constant.RESOURCE_TYPE_DISK)) {
            if (dateTime.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorDiskInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorDiskInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfo = serverInfoDao.getMonitorDiskInfo(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                List<Long> serverInfoWeb = serverInfoDao.getMonitorDiskInfo(serverInfoQuery);
                return serverInfoWeb;
            }
        }
        return null;
    }

    @Override
    public long selectMonitorInfoCount(String tablename, String monitorCompunent, String filter_by) {
        if (monitorCompunent.equals(Constant.RESOURCE_TYPE_CPU)) {
            if (filter_by.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorCpuInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorCpuInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorCpuInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorCpuInfoCount(serverInfoQuery);
                return serverInfo;
            }
        }
        else if (monitorCompunent.equals(Constant.RESOURCE_TYPE_MEMORY)) {
            if (filter_by.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorMemoryInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorMemoryInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorMemoryInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorMemoryInfoCount(serverInfoQuery);
                return serverInfo;
            }
        }
        else if (monitorCompunent.equals(Constant.RESOURCE_TYPE_DISK)) {
            if (filter_by.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorDiskInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorDiskInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorDiskInfoCount(serverInfoQuery);
                return serverInfo;
            }
            else if (filter_by.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                long serverInfo = serverInfoDao.getMonitorDiskInfoCount(serverInfoQuery);
                return serverInfo;
            }
        }
        return 0;
    }

    @Override
    public MemoryClear clearMemoryInfo(ServerHost oneServerHost) {
        MemoryClear clearMemoryInfo = clearMemoryInfo(oneServerHost.getIpaddress(), oneServerHost.getUsername(),
                oneServerHost.getPassword());
        return clearMemoryInfo;
    }

    private MemoryClear clearMemoryInfo(String ipaddress, String username, String password) {
        if (ipaddress == null) {
            logger.info("IP地址不能为空");
        }
        if (username == null) {
            logger.info("用户名不能为空");
        }
        if (password == null) {
            logger.info("密码不能为空");
        }
        password = DesUtils.decoded(password, Constant.encodeKey);
        Connection conn = null;
        boolean isAuthenticated = false;
        try {
            conn = new Connection(ipaddress); // hostname
            conn.connect();
            isAuthenticated = conn.authenticateWithPassword(username, password); // 你要远程登录的主机的用户名及密码,如admin/123456
            if (isAuthenticated == false)
                System.out.println("SSH Login Authentication failed.");
            else {
                Session sess = conn.openSession();
                ClearMemoryInfo clearMemoryInfo = new ClearMemoryInfo();
                clearMemoryInfo.syncInfo(sess);
                sess.close();
                sess = conn.openSession();
                clearMemoryInfo.clearMemoryInfo(sess);
                sess.close();
                sess = conn.openSession();
                clearMemoryInfo.rollbackFile(sess);
                sess.close();
                sess = conn.openSession();
                SysMemInfo sysMemInfo = new SysMemInfo(sess);
                int useValue = sysMemInfo.getUseValue();
                useValue = useValue / 1024;
                int memInfo = sysMemInfo.getMEMInfo();
                MemoryClear memoryClear = new MemoryClear();
                memoryClear.setUseValue(useValue);
                memoryClear.setPercentage(memInfo);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = sdf.format(new Date());
                memoryClear.setDatetime(dateTime);
                sess.close();
                return memoryClear;
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public List<String> getMonitorInfoTime(String tablename, String monitorCompunent, String dateTime) {
        if (monitorCompunent.equals(Constant.RESOURCE_TYPE_CPU)) {
            if (dateTime.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorCpuInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorCpuInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorCpuInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfoWeb = serverInfoDao.getMonitorCpuInfoTime(serverInfoQuery);
                return serverInfoWeb;
            }
        }
        else if (monitorCompunent.equals(Constant.RESOURCE_TYPE_MEMORY)) {
            if (dateTime.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorMemoryInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorMemoryInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorMemoryInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfoWeb = serverInfoDao.getMonitorMemoryInfoTime(serverInfoQuery);
                return serverInfoWeb;
            }
        }
        else if (monitorCompunent.equals(Constant.RESOURCE_TYPE_DISK)) {
            if (dateTime.equals(Constant.MONITORY_INFO_HOUR)) {
                ServerInfoQuery serverInfoQuery = queryOneHour();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorDiskInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_DAY)) {
                ServerInfoQuery serverInfoQuery = queryOneDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorDiskInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_SEVEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = querySevenDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfo = serverInfoDao.getMonitorDiskInfoTime(serverInfoQuery);
                return serverInfo;
            }
            else if (dateTime.equals(Constant.MONITORY_INFO_FIFTEEN_DAYS)) {
                ServerInfoQuery serverInfoQuery = queryFifteenDay();
                serverInfoQuery.setTablename(tablename);
                List<String> serverInfoWeb = serverInfoDao.getMonitorDiskInfoTime(serverInfoQuery);
                return serverInfoWeb;
            }
        }
        return null;
    }
}
