package com.jk.ndt.etl.service.presystem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.dao.presystem.MBusinessProcessSyncDao;
import com.jk.ndt.etl.entity.presystem.MBusinessProcessSync;
import com.jk.ndt.etl.service.presystem.PreSystemService;

@Service("preSystemService")
public class PreSystemServiceImpl implements PreSystemService {

	@Autowired
	MBusinessProcessSyncDao mBusinessProcessSyncDao;

	@Override
	public void saveOneMBusinessProcessSync(MBusinessProcessSync mBusinessProcessSync) {
		mBusinessProcessSyncDao.saveMBusinessProcessSync(mBusinessProcessSync);
	}

	public MBusinessProcessSync getNTopOneUnProcessedM() {
		return mBusinessProcessSyncDao.getNTopOneUnProcessedM();
	}

	public int updateMBusinessProcessSync(MBusinessProcessSync mBusinessProcessSync) {
		return mBusinessProcessSyncDao.updateMBusinessProcessSync(mBusinessProcessSync);
	}

}
