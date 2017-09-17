package com.jk.ndt.etl.service.system;

import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.entity.system.Resource;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface ResourceService {

    public Resource getResource(BigDecimal resource_id);

    public Resource getResourceByNameOrId(Resource resource);

    public List<Resource> listResource(Resource resource, Page page);

    public int saveResource(Resource resource);

    public int updateResource(Resource resource);

    public int deleteResource(BigDecimal resource_id);

    public int getResourceByKey(Resource resource);

    public BigDecimal getResourceSequence();

}
