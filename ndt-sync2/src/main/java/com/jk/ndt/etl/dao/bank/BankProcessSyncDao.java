package com.jk.ndt.etl.dao.bank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jk.ndt.etl.entity.bank.BankProcessSync;

public interface BankProcessSyncDao {
    public int saveBankProcessSync(BankProcessSync bankProcessSync);

    public BankProcessSync getNTopOneUnProcessedM();

    public int updateBankProcessSync(BankProcessSync bankProcessSync);

    /**
     * 
     * @Description: 根据条件查询银行数据
     * @author fangwei
     * @date 2017年6月8日 上午8:37:12
     * @param map
     * @return
     */
    public List<BankProcessSync> listByParam(@Param("map") Map<String, Object> map);
}
