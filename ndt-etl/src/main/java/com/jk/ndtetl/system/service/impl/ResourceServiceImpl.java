package com.jk.ndtetl.system.service.impl;

import java.util.List;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.system.Resource;
import com.jk.ndtetl.system.dao.ResourceDao;
import com.jk.ndtetl.system.service.IResourceService;

/**
 * Created by 朱生 on 2017/5/13.
 */
@Service("resourceService")
public class ResourceServiceImpl implements IResourceService {

    @Autowired
    private ResourceDao resourceDao;

    @Override
    public Resource getResource(Integer resource_id) {
        return resourceDao.getResource(resource_id);
    }

    @Override
    public Resource getResourceByNameOrId(Resource resource) {
        return resourceDao.getResourceByNameOrId(resource);
    }

    @Override
    public List<Resource> listResource(Resource resource, BasePage basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        return resourceDao.listResource(resource);
    }

    @Override
    public List<Resource> listAllResource() {
        return resourceDao.listAllResource();
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
    public int deleteResource(Integer resource_id) {
        return resourceDao.deleteResource(resource_id);
    }

    @Override
    public int getResourceByKey(Resource resource) {
        return resourceDao.getResourceByKey(resource);
    }

    @Override
    public Integer getResourceSequence() {
        return resourceDao.getResourceSequence();
    }

}
