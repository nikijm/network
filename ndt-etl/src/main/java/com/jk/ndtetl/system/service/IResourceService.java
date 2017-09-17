package com.jk.ndtetl.system.service;

import com.github.pagehelper.Page;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.system.Resource;

import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface IResourceService {

    public Resource getResource(Integer resource_id);

    public Resource getResourceByNameOrId(Resource resource);

    public List<Resource> listResource(Resource resource, BasePage page);

    public List<Resource> listAllResource();

    public int saveResource(Resource resource);

    public int updateResource(Resource resource);

    public int deleteResource(Integer resource_id);

    public int getResourceByKey(Resource resource);

    public Integer getResourceSequence();

}
