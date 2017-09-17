package com.jk.ndtetl.mon.service;

import com.jk.ndtetl.mon.ServerInfo;

/**
 * 
 * @ClassName: ServerInfoService
 * @Description: 操作监控信息的服务接口
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServerInfoService {

    /**
     * 插入主机的信息
     * 
     * @param serverInfo
     */
    public void insertServerInfo(ServerInfo serverInfo);

    public void deleteMonitorInfoLtFifteen(ServerInfo serverInfo);

}
