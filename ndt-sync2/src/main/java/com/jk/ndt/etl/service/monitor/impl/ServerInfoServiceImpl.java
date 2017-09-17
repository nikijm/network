package com.jk.ndt.etl.service.monitor.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.dao.monitor.ServerInfoDao;
import com.jk.ndt.etl.entity.monitor.ServerInfo;
import com.jk.ndt.etl.service.monitor.ServerInfoService;
/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 监控信息的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("serverInfoService")
public class ServerInfoServiceImpl implements ServerInfoService{
	
	@Autowired
	ServerInfoDao serverInfoDao;

	@Override
	public void insertServerInfo(ServerInfo serverInfo) {
		
		serverInfoDao.insertServerInfo(serverInfo);
		
	}

}
