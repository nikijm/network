package com.jk.ndtetl.mon.dao;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.mon.ServiceInfo;

/**
 * 
 * @ClassName: ServiceManagerDao
 * @Description: 服务的dao
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServiceManagerDao extends BaseDao<ServiceInfo> {

    /**
     * 保存服务信息
     * 
     * @param serviceInfo
     */
    void saveServiceInfo(ServiceInfo serviceInfo);

    /**
     * 启动服务
     * 
     * @param id
     */
    void updateServiceStatuStart(Integer id);

    /**
     * 停止服务
     * 
     * @param id
     */
    void updateServiceStatuStop(Integer id);

    /**
     * 服务停止处理中
     * 
     * @param id
     */
    void updateServiceStatuProcessing(Integer id);

    /**
     * 查询服务是否可用
     * 
     * @param resourceId
     * @return
     */
    ServiceInfo getServiceStatus(Integer resourceId);

}
