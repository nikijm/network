package com.jk.ndtetl.etl.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;

/**
 * @ClassName: IDDLExecutorService
 * @Description: ddl相关的方法
 * @author lianhanwen
 * @date 2017年6月22日 下午5:41:32
 *
 */
public interface IDDLExecutorService {

    /**
     * @Description: 创建表
     * @author lianhanwen
     * @date 2017年5月18日 下午2:44:38
     * @param dataTable
     * @return
     */
    void createTable(TableDef tableDef);

    /**
     * @Description: 添加列
     * @author lianhanwen
     * @date 2017年6月24日 下午1:40:51
     * @param tableDef
     */
    void addColumn(String tableName, List<ColumnDef> columns);

    /**
     * @Description: 修改表
     * @author lianhanwen
     * @date 2017年6月24日 下午1:40:55
     * @param tableDef
     */
    void alterTable(TableDef tableDef);

    /**
     * @Description: 创建序列
     * @author lianhanwen
     * @date 2017年6月13日 下午6:43:30
     * @param tableName
     */
    void createSeq(String tableName);

    /**
     * 
     * @Description: 根据表名删除表
     * @author lianhanwen
     * @date 2017年5月22日 下午5:26:08
     * @param tableName
     */
    void deleteTable(@Param("tableName") String tableName);

    /**
     * 
     * @Description: 根据表名删除相应表的序列
     * @author lianhanwen
     * @date 2017年5月22日 下午5:26:34
     * @param tableName
     */
    void deleteSeq(@Param("tableName") String tableName);

    /**
     * 复制表
     * 
     * @author lianhanwen
     * @date 2017年7月17日 上午10:01:02
     * @param sourceTableName
     * @param targetTableName
     */
    void copyTable(String sourceTableName, String targetTableName);

}
