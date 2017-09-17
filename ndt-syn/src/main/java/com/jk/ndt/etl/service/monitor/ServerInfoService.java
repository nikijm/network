package com.jk.ndt.etl.service.monitor;

import com.jk.ndt.etl.entity.monitor.ServerInfo;

public interface ServerInfoService {

	/**
	 * 插入主机的信息
	 * 
	 * @param serverInfo
	 */
	public void insertServerInfo(ServerInfo serverInfo);

}
