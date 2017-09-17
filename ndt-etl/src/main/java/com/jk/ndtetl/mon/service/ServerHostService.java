package com.jk.ndtetl.mon.service;

import java.util.List;

import com.jk.ndtetl.mon.ServerHost;

/**
 * 
 * @ClassName: ServerHostService
 * @Description: 操作服务主机的接口
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServerHostService {

    public ServerHost getServerHostListByIp(String ipAddress);

    /**
     * 获取可用主机的列表
     * 
     * @return
     */
    public List<ServerHost> listServerhostInfoWithStatu();

    /**
     * 获取所有主机的列表
     * 
     * @return
     */
    public List<ServerHost> listServerhostInfoNotStatu();

    // public void updateServerhostUserInfo(Integer serverId, String username,
    // String password);

    /**
     * 跟新主机的信息
     * 
     * @param serverHost
     */
    public void updateServerhostInfo(ServerHost serverHost);

    /**
     * 保存主机的信息
     * 
     * @param serverHost
     */
    public void saveServerhostInfo(ServerHost serverHost);

    /**
     * 主机的信息
     * 
     * @param serverId
     * @return 主机的实体类
     */
    public ServerHost getOneServerHost(Integer serverId);

    /**
     * 获取一个主机信息
     * 
     * @param serverId
     * @return
     */
    public ServerHost getOneServerhostInfo(Integer serverId);

    /**
     * 删除主机的信息
     * 
     * @param id
     */
    public void deleteServerHostInfo(Integer id);

}
