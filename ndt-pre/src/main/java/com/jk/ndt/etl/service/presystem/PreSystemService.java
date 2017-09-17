package com.jk.ndt.etl.service.presystem;

import java.util.List;

import com.jk.ndt.etl.entity.presystem.BusinessProcessSync;
import com.jk.ndt.etl.entity.presystem.BusinessSync;

public interface PreSystemService {
	public void saveOneMBusinessProcessSync(BusinessProcessSync mBusinessProcessSync);

	public BusinessProcessSync getNTopOneUnProcessedApplicationM();

	public int updateMBusinessProcessSync(BusinessProcessSync mBusinessProcessSync);

	public List<BusinessProcessSync> applyList();

	public List<BusinessProcessSync> listByDocumentNo(String documentNo);

	public BusinessSync getBusinessSyncById(Long businesssyncId);

	public BusinessSync getBusinessSyncByBusinessCode(String businessCode);
	
	public List<BusinessSync> getBusinessSyncList();
}
