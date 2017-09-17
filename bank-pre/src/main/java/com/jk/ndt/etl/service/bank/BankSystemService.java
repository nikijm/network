package com.jk.ndt.etl.service.bank;

import java.util.List;

import com.jk.ndt.etl.entity.bank.BankProcessSync;

public interface BankSystemService {
	public void saveOneBankProcessSync(BankProcessSync bankProcessSync);

	public BankProcessSync getNTopOneUnProcessedBusinessOperate();

	public BankProcessSync getNTopOneUnProcessedApplication();

	public int updateBankProcessSync(BankProcessSync bankProcessSync);

	public List<BankProcessSync> applyList();

}
