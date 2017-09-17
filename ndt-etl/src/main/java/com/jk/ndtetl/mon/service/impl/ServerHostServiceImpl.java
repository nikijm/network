package com.jk.ndtetl.mon.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.jk.ndtetl.Constant;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.mon.AlarmRuleBean;
import com.jk.ndtetl.mon.MonitorTableInfo;
import com.jk.ndtetl.mon.ServerHost;
import com.jk.ndtetl.mon.dao.ServerHostDao;
import com.jk.ndtetl.mon.service.AlarmRuleService;
import com.jk.ndtetl.mon.service.LogManagerService;
import com.jk.ndtetl.mon.service.ServerHostService;
import com.jk.ndtetl.util.DesUtils;

import java.util.List;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 服务主机的service
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("serverHostService")
public class ServerHostServiceImpl implements ServerHostService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ServerHostDao serverHostDao;

    @Autowired
    CommonDao commonDao;

    @Autowired
    LogManagerService logManagerService;

    @Autowired
    AlarmRuleService alarmRuleService;

    @Override
    public ServerHost getServerHostListByIp(String ipAddress) {
        ServerHost serverBean = serverHostDao.getServerHostListByIp(ipAddress);
        return serverBean;
    }

    @Override
    public List<ServerHost> listServerhostInfoWithStatu() {
        List<ServerHost> listServerhostInfo = serverHostDao.listServerhostInfoWithStatu();
        return listServerhostInfo;
    }

    @Override
    public List<ServerHost> listServerhostInfoNotStatu() {
        List<ServerHost> listServerhostInfo = serverHostDao.listServerhostInfoNotStatu();
        return listServerhostInfo;
    }

    // @Override
    // public void updateServerhostUserInfo(Integer serverId, String username,
    // String password) {
    // if(serverId!=null){
    // ServerHost serverHost=new ServerHost();
    // serverHost.setId(serverId);
    // serverHost.setUsername(username);
    // String encoded = DesUtils.encoded(password, Constant.encodeKey);
    // serverHost.setPassword(encoded);
    // serverHostDao.updateServerhostUserInfo(serverHost);
    // }
    // }

    @Override
    public void updateServerhostInfo(ServerHost serverHost) {
        if (serverHost.getId() != null) {
            serverHost.setPassword(DesUtils.encoded(serverHost.getPassword(), Constant.encodeKey));
            serverHostDao.updateServerhostInfo(serverHost);
            String description = serverHost.getDescription() == null ? "" : serverHost.getDescription();
            logManagerService.info("对主机" + description + "进行了修改");
        }
        else {
            logger.info("id 不能为空");
        }
    }

    /**
     * 保存host信息
     * @param serverHost
     */
    @Override
    public void saveServerhostInfo(ServerHost serverHost) {
        String encoded = DesUtils.encoded(serverHost.getPassword(), Constant.encodeKey);
        serverHost.setPassword(encoded);
        serverHostDao.insertServerHost(serverHost);
        MonitorTableInfo monitorTableInfo = new MonitorTableInfo();
        monitorTableInfo.setTableName(serverHost.getTablename());
        serverHostDao.createTable(monitorTableInfo);
        // serverHostDao.createSeq(monitorTableInfo);
        // serverHostDao.createTrigger(monitorTableInfo);
        System.out.println("nihao");
    }

    @Override
    public ServerHost getOneServerHost(Integer serverId) {
        if (serverId == null) {
            logger.error("serverId不能为空");
        }
        ServerHost getServerhostInfo = serverHostDao.getServerhostInfo(serverId);
        return getServerhostInfo;
    }

    @Override
    public ServerHost getOneServerhostInfo(Integer serverId) {
        ServerHost serverhostInfo = serverHostDao.getServerhostInfo(serverId);
        return serverhostInfo;
    }

    @Override
    public void deleteServerHostInfo(Integer id) {
        ServerHost serverhostInfo = serverHostDao.getServerhostInfo(id);
        serverHostDao.deleteServerHostInfo(id);
        AlarmRuleBean alarmRuleBean = new AlarmRuleBean();
        alarmRuleBean.setMachineAndId(id);
        alarmRuleService.deleteAlarmRuleByServerId(alarmRuleBean);
        String description = serverhostInfo.getDescription() == null ? "" : serverhostInfo.getDescription();
        logManagerService.info("对主机" + description + "进行了删除");
        // commonDao.deleteTable(serverhostInfo.getTablename());
        // commonDao.deleteSeq(serverhostInfo.getTablename());
    }

}
