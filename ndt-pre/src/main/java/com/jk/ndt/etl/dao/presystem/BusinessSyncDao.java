package com.jk.ndt.etl.dao.presystem;

import java.util.List;

import com.jk.ndt.etl.entity.presystem.BusinessSync;

public interface BusinessSyncDao {
	public BusinessSync getBusinessSyncById(Long businesssyncId);

	public BusinessSync getBusinessSyncByBusinessCode(String businessCode);

	public List<BusinessSync> getBusinessSyncList();
}
