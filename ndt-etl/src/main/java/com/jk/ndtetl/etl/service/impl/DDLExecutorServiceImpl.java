package com.jk.ndtetl.etl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.etl.dao.DDLExecutorDao;
import com.jk.ndtetl.etl.service.IDDLExecutorService;

/**
 * @ClassName: DDLExecutorServiceImpl
 * @Description: ddl操作的相关方法实现
 * @author lianhanwen
 * @date 2017年6月22日 下午5:42:22
 *
 */
@Service("dDLExecutorService")
public class DDLExecutorServiceImpl implements IDDLExecutorService {

    @Autowired
    private DDLExecutorDao ddlExecutorDao;

    /**
     * @Description: 根据sql创建表
     * @author lianhanwen
     * @date 2017年5月18日 下午2:44:38
     * @param dataTable
     * @return
     */
    @Override
    public void createTable(TableDef tableDef) {
        ddlExecutorDao.createTable(tableDef);
        ddlExecutorDao.createSeq(tableDef.getTableName());
    }

    /**
     * 
     * @Description: 根据表名删除表
     * @author lianhanwen
     * @date 2017年5月22日 下午5:26:08
     * @param tableName
     */
    @Override
    public void deleteTable(String tableName) {
        ddlExecutorDao.deleteTable(tableName);
        ddlExecutorDao.deleteSeq(tableName);
    }

    /**
     * 
     * @Description: 根据表名删除相应表的序列
     * @author lianhanwen
     * @date 2017年5月22日 下午5:26:34
     * @param tableName
     */
    @Override
    public void deleteSeq(String tableName) {
        ddlExecutorDao.deleteSeq(tableName);
    }

    /**
     * @Description: 创建序列
     * @author lianhanwen
     * @date 2017年6月13日 下午6:43:30
     * @param tableName
     */
    @Override
    public void createSeq(String tableName) {
        ddlExecutorDao.createSeq(tableName);
    }

    /**
     * @param tableDef
     * @Description: 添加列
     * @author lianhanwen
     * @date 2017年6月24日 下午1:40:51
     */
    @Override
    public void addColumn(String tableName, List<ColumnDef> columns) {

        ddlExecutorDao.addColumn(tableName, columns);
    }

    /**
     * @Description: 修改表
     * @author lianhanwen
     * @date 2017年6月24日 下午1:40:55
     * @param tableDef
     */
    @Override
    public void alterTable(TableDef tableDef) {
        ddlExecutorDao.alterTable(tableDef);
    }

    /**
     *
     * @param sourceTableName  源表:被复制的表
     * @param targetTableName  目标表：根据源表复制创建的新表
     */
    @Override
    public void copyTable(String sourceTableName, String targetTableName) {
        ddlExecutorDao.copyTable(sourceTableName, targetTableName);
    }

}
