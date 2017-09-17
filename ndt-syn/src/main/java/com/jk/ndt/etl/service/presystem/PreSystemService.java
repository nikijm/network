package com.jk.ndt.etl.service.presystem;

import com.jk.ndt.etl.entity.presystem.MBusinessProcessSync;

public interface PreSystemService {
	public void saveOneMBusinessProcessSync(MBusinessProcessSync mBusinessProcessSync);

	public MBusinessProcessSync getNTopOneUnProcessedM();

	public int updateMBusinessProcessSync(MBusinessProcessSync mBusinessProcessSync);
}
