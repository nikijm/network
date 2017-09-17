package com.jk.ndt.etl.dao.presystem;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jk.ndt.etl.entity.presystem.BusinessProcessSync;

public interface MBusinessProcessSyncDao {
	public int saveBusinessProcessSync(BusinessProcessSync mBusinessProcessSync);

	public BusinessProcessSync getNTopOneUnProcessedM();

	public int updateBusinessProcessSync(BusinessProcessSync mBusinessProcessSync);

    public List<BusinessProcessSync> listByParam(@Param("map") Map<String,Object> map);
}
