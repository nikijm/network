package com.jk.ndt.etl.service.bank;

import java.util.List;

import com.jk.ndt.etl.entity.bank.BankProcessSync;

public interface BankSystemService {
	public void saveOneBankProcessSync(BankProcessSync bankProcessSync);

	public BankProcessSync getNTopOneUnProcessedM();

	public int updateBankProcessSync(BankProcessSync bankProcessSync);
	
	public List<BankProcessSync> applyList();
	
	public List<BankProcessSync> listByDocumentNo(Long recordId);
}
