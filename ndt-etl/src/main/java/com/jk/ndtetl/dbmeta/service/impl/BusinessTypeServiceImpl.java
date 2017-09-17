package com.jk.ndtetl.dbmeta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.dbmeta.BusinessType;
import com.jk.ndtetl.dbmeta.dao.BusinessTypeDao;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;

import java.util.List;
import java.util.Map;

/**
 * DataFileTypeService的实现类
 * @ClassName: DataFileTypeServiceImpl 
 * @author lianhanwen 
 * @date 2017年6月27日 上午9:52:28 
 *
 */
@Service("businessTypeService")
public class BusinessTypeServiceImpl extends BaseServiceImpl<BusinessType> implements IBusinessTypeService {
    @Autowired
    private BusinessTypeDao businessTypeDao;

    @Override
    protected BaseDao<BusinessType> getDao() {
        return businessTypeDao;
    }

    @Override
    public List<Map<String, Object>> listOptionByParam(Map<String, Object> param) {
        return businessTypeDao.listOptionByParam(param);
    }
}
