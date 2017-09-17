package com.jk.ndtetl.mon.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.mon.ServerInfo;
import com.jk.ndtetl.mon.dao.ServerInfoDao;
import com.jk.ndtetl.mon.service.ServerInfoService;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控信息的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("serverInfoService")
public class ServerInfoServiceImpl implements ServerInfoService {

    @Autowired
    ServerInfoDao serverInfoDao;

    @Override
    public void insertServerInfo(ServerInfo serverInfo) {

        serverInfoDao.insertServerInfo(serverInfo);

    }

    @Override
    public void deleteMonitorInfoLtFifteen(ServerInfo serverInfo) {
        Long calculate = new Date().getTime();
        calculate = calculate - 15 * 24 * 60 * 60 * 1000;
        serverInfo.setCalculate(calculate);
        serverInfoDao.deleteMonitorInfoLtFifteen(serverInfo);
    }

}
