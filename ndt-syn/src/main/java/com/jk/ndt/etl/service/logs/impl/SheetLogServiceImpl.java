package com.jk.ndt.etl.service.logs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.dao.logs.SheetLogDao;
import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.service.BaseServiceImpl;
import com.jk.ndt.etl.service.logs.SheetLogService;

@Service("sheetLogService")
public class SheetLogServiceImpl extends BaseServiceImpl<SheetLog> implements SheetLogService {
    @Autowired
    private SheetLogDao sheetLogDao;

    @Override
    protected BaseDao<SheetLog> getDao() {
        return sheetLogDao;
    }

    @Override
    public void deleteCacheLogBySheetId(Integer sheetId) {
        sheetLogDao.deleteCacheLogBySheetId(sheetId);
    }

}
