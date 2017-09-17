package com.jk.ndtetl.mon.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.mon.ServiceInfo;
import com.jk.ndtetl.mon.dao.ServiceManagerDao;
import com.jk.ndtetl.mon.service.ServiceManagerService;
import com.jk.ndtetl.schedule.pool.EtlThreadPoolTaskExecutor;

/**
 * 
 * @ClassName: ServiceManagerServiceImpl
 * @Description: 监控服务的service
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("serviceManagerService")
public class ServiceManagerServiceImpl extends BaseServiceImpl<ServiceInfo> implements ServiceManagerService {

    @Autowired
    ServiceManagerDao serviceManagerDao;

    @Resource(name = "cacheThreadPool")
    private EtlThreadPoolTaskExecutor cacheThreadPool;

    @Resource(name = "cleanThreadPool")
    private EtlThreadPoolTaskExecutor cleanThreadPool;

    @Resource(name = "transformThreadPool")
    private EtlThreadPoolTaskExecutor transformThreadPool;

    @Resource(name = "verifyThreadPool")
    private EtlThreadPoolTaskExecutor verifyThreadPool;

    @Override
    protected BaseDao<ServiceInfo> getDao() {
        return serviceManagerDao;
    }

    @Override
    public void updateServiceStatuStart(Integer id) {
        serviceManagerDao.updateServiceStatuStart(id);
    }

    @Override
    public void updateServiceStatuStop(Integer id) {
        serviceManagerDao.updateServiceStatuStop(id);
    }

    @Override
    public void serviceUpdateStatuProcessing(Integer id) {
        serviceManagerDao.updateServiceStatuProcessing(id);
        ServiceInfo byId = serviceManagerDao.getById(id);
        if (byId != null) {
            Integer resourceId = byId.getResourceId();
            switch (resourceId) {
            case 1:
                updateStatusToStopCache(id);
                break;
            case 2:
                updateStatusToStopClear(id);
                break;
            case 3:
                updateStatusToStopContrast(id);
                break;
            case 4:
                updateStatusToStopConversion(id);
                break;
            default:
                break;
            }
        }
    }

    private void updateStatusToStopCache(Integer id) {
        Integer integer = new Integer(cacheThreadPool.getActiveCount());
        while (!integer.equals(0)) {
            integer = new Integer(cacheThreadPool.getActiveCount());
        }
        serviceManagerDao.updateServiceStatuStop(id);
    }

    private void updateStatusToStopClear(Integer id) {
        Integer integer = new Integer(cleanThreadPool.getActiveCount());
        while (!integer.equals(0)) {
            integer = new Integer(cleanThreadPool.getActiveCount());
        }
        serviceManagerDao.updateServiceStatuStop(id);
    }

    private void updateStatusToStopContrast(Integer id) {
        Integer integer = new Integer(transformThreadPool.getActiveCount());
        while (!integer.equals(0)) {
            integer = new Integer(transformThreadPool.getActiveCount());
        }
        serviceManagerDao.updateServiceStatuStop(id);
    }

    private void updateStatusToStopConversion(Integer id) {
        Integer integer = new Integer(verifyThreadPool.getActiveCount());
        while (!integer.equals(0)) {
            integer = new Integer(verifyThreadPool.getActiveCount());
        }
        serviceManagerDao.updateServiceStatuStop(id);
    }

    @Override
    public Integer getServiceStatus(Integer resourceId) {
        ServiceInfo serviceStatus = serviceManagerDao.getServiceStatus(resourceId);
        if (serviceStatus != null && serviceStatus.getStatu() != null) {
            return serviceStatus.getStatu();
        }
        return null;
    }

}
