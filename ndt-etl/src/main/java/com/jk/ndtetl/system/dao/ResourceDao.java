package com.jk.ndtetl.system.dao;

import java.util.List;

import com.jk.ndtetl.system.Resource;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface ResourceDao {

    public Resource getResource(Integer role_id);

    public Resource getResourceByNameOrId(Resource resource);

    public List<Resource> listResource(Resource resource);

    public List<Resource> listAllResource();

    public int saveResource(Resource resource);

    public int updateResource(Resource resource);

    public int deleteResource(Integer etl_resource_id);

    public Integer getResourceSequence();

    public int getResourceByKey(Resource resource);


}
