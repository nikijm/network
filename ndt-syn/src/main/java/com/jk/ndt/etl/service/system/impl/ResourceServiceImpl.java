package com.jk.ndt.etl.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.jk.ndt.etl.dao.system.ResourceDao;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.service.system.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceDao resourceDao;

    @Override
    public Resource getResource(BigDecimal resource_id) {
        return resourceDao.getResource(resource_id);
    }

    @Override
    public Resource getResourceByNameOrId(Resource resource) {
        return resourceDao.getResourceByNameOrId(resource);
    }

    @Override
    public List<Resource> listResource(Resource resource, Page basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        return resourceDao.listResource(resource);
    }

    @Override
    public int saveResource(Resource resource) {
        return resourceDao.saveResource(resource);
    }

    @Override
    public int updateResource(Resource resource) {
        return resourceDao.updateResource(resource);
    }

    @Override
    public int deleteResource(BigDecimal resource_id) {
        return resourceDao.deleteResource(resource_id);
    }

    @Override
    public int getResourceByKey(Resource resource) {
        return resourceDao.getResourceByKey(resource);
    }

    @Override
    public BigDecimal getResourceSequence() {
        return resourceDao.getResourceSequence();
    }

}
