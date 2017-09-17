package com.jk.ndt.etl.service.bank.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.dao.bank.BankProcessSyncDao;
import com.jk.ndt.etl.entity.bank.BankProcessSync;
import com.jk.ndt.etl.service.bank.BankSystemService;

@Service("bankSystemService")
public class BankSystemServiceImpl implements BankSystemService {

	@Autowired
	BankProcessSyncDao bankProcessSyncDao;

	@Override
	public void saveOneBankProcessSync(BankProcessSync bankProcessSync) {
		bankProcessSyncDao.saveBankProcessSync(bankProcessSync);
	}

	public BankProcessSync getNTopOneUnProcessedBusinessOperate() {
		return bankProcessSyncDao.getNTopOneUnProcessedBusinessOperate();
	}

	public BankProcessSync getNTopOneUnProcessedApplication() {
		return bankProcessSyncDao.getNTopOneUnProcessedApplication();
	}

	public int updateBankProcessSync(BankProcessSync bankProcessSync) {
		return bankProcessSyncDao.updateBankProcessSync(bankProcessSync);
	}

	@Override
	public List<BankProcessSync> applyList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeCode", 9100);
		return bankProcessSyncDao.listByParam(map);
	}

}
