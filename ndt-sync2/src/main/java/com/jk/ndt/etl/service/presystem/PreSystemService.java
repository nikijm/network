package com.jk.ndt.etl.service.presystem;

import java.util.List;

import com.jk.ndt.etl.entity.presystem.BusinessProcessSync;

public interface PreSystemService {
	public void saveOneMBusinessProcessSync(BusinessProcessSync mBusinessProcessSync);

	public BusinessProcessSync getNTopOneUnProcessedM();

	public int updateMBusinessProcessSync(BusinessProcessSync mBusinessProcessSync);
	
	public List<BusinessProcessSync> applyList();
	
	public List<BusinessProcessSync> listByDocumentNo(String documentNo);
}
