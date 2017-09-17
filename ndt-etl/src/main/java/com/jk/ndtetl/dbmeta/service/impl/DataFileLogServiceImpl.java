package com.jk.ndtetl.dbmeta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.dbmeta.DataFileLog;
import com.jk.ndtetl.dbmeta.dao.DataFileLogDao;
import com.jk.ndtetl.dbmeta.service.IDataFileLogService;

/**
 * 
 * @ClassName: DataFileLogServiceImpl
 * @author lianhanwen
 * @date 2017年7月3日 下午2:52:51
 *
 */
@Service("dataFileLogService")
public class DataFileLogServiceImpl extends BaseServiceImpl<DataFileLog> implements IDataFileLogService {

    @Autowired
    private DataFileLogDao dataFileLogDao;

    @Override
    protected BaseDao<DataFileLog> getDao() {
        return dataFileLogDao;
    }

    @Override
    public DataFileLog getErrorLogByFileId(Integer dataFileId) {
        List<DataFileLog> dataFileLogs = dataFileLogDao.getByFileId(dataFileId);
        if (dataFileLogs != null && dataFileLogs.size() > 0) {
            return dataFileLogs.get(0);
        }
        return null;
    }

}
