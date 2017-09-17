package com.jk.ndt.etl.service.monitor.impl;

import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.dao.common.CommonDao;
import com.jk.ndt.etl.dao.monitor.ServerHostDao;
import com.jk.ndt.etl.entity.monitor.MonitorTableInfo;
import com.jk.ndt.etl.entity.monitor.ServerHost;
import com.jk.ndt.etl.service.monitor.ServerHostService;
import com.jk.ndt.etl.utility.DesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
public class ServerHostServiceImpl implements ServerHostService{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ServerHostDao serverHostDao;
	@Autowired
	CommonDao commonDao;

	@Override
	public ServerHost getServerHostListByIp(String ipAddress) {
		ServerHost serverBean = serverHostDao.getServerHostListByIp(ipAddress);
		return serverBean;
	}

	@Override
	public List<ServerHost> listServerhostInfo() {
		List<ServerHost> listServerhostInfo = serverHostDao.listServerhostInfo();
		return listServerhostInfo;
	}

	@Override
	public void updateServerhostUserInfo(Integer serverId, String username, String password) {
		if(serverId!=null){
			ServerHost serverHost=new ServerHost();
			serverHost.setId(serverId);
			serverHost.setUsername(username);
			String encoded = DesUtils.encoded(password, Constant.encodeKey);
			serverHost.setPassword(encoded);
			serverHostDao.updateServerhostUserInfo(serverHost);
		}
	}

	@Override
	public void updateServerhostInfo(ServerHost serverHost) {
		if(serverHost.getId()!=null){
			serverHost.setPassword(DesUtils.encoded(serverHost.getPassword(), Constant.encodeKey));
			serverHostDao.updateServerhostInfo(serverHost);
		}else{
			logger.info("id 不能为空");
		}
	}

	@Override
	public void saveServerhostInfo( ServerHost serverHost) {
		String encoded = DesUtils.encoded(serverHost.getPassword(), Constant.encodeKey);
		serverHost.setPassword(encoded);
		serverHostDao.insertServerHost(serverHost);
		MonitorTableInfo monitorTableInfo=new MonitorTableInfo();
		monitorTableInfo.setTableName(serverHost.getTablename());
		serverHostDao.createTable(monitorTableInfo);
		serverHostDao.createSeq(monitorTableInfo);
		serverHostDao.createTrigger(monitorTableInfo);
	}

	@Override
	public ServerHost getOneServerHost(Integer serverId) {
		if(serverId==null){
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
		commonDao.deleteTable(serverhostInfo.getTablename());
		commonDao.deleteSeq(serverhostInfo.getTablename());
	}

}
