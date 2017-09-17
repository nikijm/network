package com.jk.ndtetl.mon.service;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.mon.ServiceInfo;

/**
 * 
 * @ClassName: ServiceManagerService
 * @Description: 操作服务的接口
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface ServiceManagerService extends IBaseService<ServiceInfo> {

    void updateServiceStatuStart(Integer id);

    void updateServiceStatuStop(Integer id);

    Integer getServiceStatus(Integer resourceId);

    public void serviceUpdateStatuProcessing(Integer id);

}
