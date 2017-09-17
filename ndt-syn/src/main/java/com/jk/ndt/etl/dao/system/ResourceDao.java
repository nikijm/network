package com.jk.ndt.etl.dao.system;

import com.jk.ndt.etl.entity.system.Resource;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface ResourceDao {

    public Resource getResource(BigDecimal role_id);

    public Resource getResourceByNameOrId(Resource Resource);

    public List<Resource> listResource(Resource role);

    public int saveResource(Resource role);

    public int updateResource(Resource role);

    public int deleteResource(BigDecimal role_id);

    public BigDecimal getResourceSequence();

    public int getResourceByKey(Resource resource);


}
