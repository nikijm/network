package com.jk.ndtetl.dbmeta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.dao.DataFileTypeDao;
import com.jk.ndtetl.dbmeta.service.IDataFileTypeService;

import java.util.List;
import java.util.Map;

/**
 * DataFileTypeService的实现类
 * @ClassName: DataFileTypeServiceImpl 
 * @author lianhanwen 
 * @date 2017年6月27日 上午9:52:28 
 *
 */
@Service("dataFileTypeService")
public class DataFileTypeServiceImpl extends BaseServiceImpl<DataFileType> implements IDataFileTypeService {
    @Autowired
    private DataFileTypeDao dataFileTypeDao;

    @Override
    protected BaseDao<DataFileType> getDao() {
        return dataFileTypeDao;
    }

    @Override
    public List<Map<String, Object>> listOptionByParam(Map<String, Object> param) {
        return dataFileTypeDao.listOptionByParam(param);
    }
}
