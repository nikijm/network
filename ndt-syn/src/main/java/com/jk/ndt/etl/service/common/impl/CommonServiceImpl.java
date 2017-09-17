package com.jk.ndt.etl.service.common.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.dao.common.CommonDao;
import com.jk.ndt.etl.entity.CommonQueryObj;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.service.common.CommonService;

/**
 * 
 * @ClassName: CommonServiceImpl
 * @Description: 公共操作类实现
 * @author fangwei
 * @date 2017年5月16日 下午3:58:23
 *
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonDao commonDao;

    @Override
    public void createTable(DataTable dataTable) {
        commonDao.createTable(dataTable);
    }

    @Override
    public List<Map<String, Object>> listAll(String tableName, String unquieId) {
        return commonDao.listAll(tableName, unquieId);
    }
    @Override
    public List<Map<String, Object>> listByPage(String tableName, String unquieId,Page page) {
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        return commonDao.listByPage(tableName, unquieId,page);
    }
    @Override
    public List<Map<String, Object>> listByIds(String tableName, String unquieId,List<Integer> ids) {
        
        return commonDao.listByIds(tableName, unquieId,ids);
    }

    @Override
    public void saveTableContent(DataTable dataTable) {
        commonDao.saveTableContent(dataTable);
//        throw new RuntimeException("报错");
    }

    @Override
    public void deleteTable(String tableName) {
        commonDao.deleteTable(tableName);  
    }

    @Override
    public void deleteSeq(String tableName) {
        commonDao.deleteSeq(tableName);        
    }

    @Override
    public List<Map<String, Object>> listByQueryObj(String tableName, List<CommonQueryObj> queryList) {
        return commonDao.listByQueryObj(tableName, queryList);
    }

    @Override
    public void createSeq(String tableName) {
        commonDao.createSeq(tableName);
    }

    

}
