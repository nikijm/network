package com.jk.ndt.etl.dao.presystem;

import com.jk.ndt.etl.entity.presystem.MBusinessProcessSync;

public interface MBusinessProcessSyncDao {
	public int saveMBusinessProcessSync(MBusinessProcessSync mBusinessProcessSync);

	public MBusinessProcessSync getNTopOneUnProcessedM();

	public int updateMBusinessProcessSync(MBusinessProcessSync mBusinessProcessSync);
}
