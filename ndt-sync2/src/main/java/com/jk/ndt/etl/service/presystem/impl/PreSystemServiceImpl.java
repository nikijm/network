package com.jk.ndt.etl.service.presystem.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.dao.presystem.MBusinessProcessSyncDao;
import com.jk.ndt.etl.entity.presystem.BusinessProcessSync;
import com.jk.ndt.etl.service.presystem.PreSystemService;

@Service("preSystemService")
public class PreSystemServiceImpl implements PreSystemService {

	@Autowired
	MBusinessProcessSyncDao mBusinessProcessSyncDao;

	@Override
	public void saveOneMBusinessProcessSync(BusinessProcessSync mBusinessProcessSync) {
		mBusinessProcessSyncDao.saveBusinessProcessSync(mBusinessProcessSync);
	}

	public BusinessProcessSync getNTopOneUnProcessedM() {
		return mBusinessProcessSyncDao.getNTopOneUnProcessedM();
	}

	public int updateMBusinessProcessSync(BusinessProcessSync mBusinessProcessSync) {
		return mBusinessProcessSyncDao.updateBusinessProcessSync(mBusinessProcessSync);
	}

    @Override
    public List<BusinessProcessSync> applyList() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("nodeCode", 9100);
        return mBusinessProcessSyncDao.listByParam(map);
    }

    @Override
    public List<BusinessProcessSync> listByDocumentNo(String documentNo) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("documentNo", documentNo);
        return mBusinessProcessSyncDao.listByParam(map);
    }

}
