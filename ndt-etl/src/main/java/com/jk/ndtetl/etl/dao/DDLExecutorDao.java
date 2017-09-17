package com.jk.ndtetl.etl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;

/**
 * @ClassName: DDLExecutor
 * @Description: 主要用于动态创建表删除表等
 * @author lianhanwen
 * @date 2017年6月22日 下午5:27:54
 *
 */
public interface DDLExecutorDao {

    /**
     * 
     * @Description: 根据解析出来的内容创建表
     * @author lianhanwen
     * @date 2017年5月18日 下午10:19:08
     * @param dataTable
     */
    void createTable(TableDef tableDef);

    /**
     * 
     * @Description: 根据表名删除表
     * @author lianhanwen
     * @date 2017年5月26日 下午12:44:17
     * @param tableName
     */
    void deleteTable(@Param("tableName") String tableName);

    /**
     * 
     * @Description: 根据表名创建序列
     * @author lianhanwen
     * @date 2017年5月18日 下午10:19:42
     * @param dataTable
     */
    void createSeq(@Param("tableName") String tableName);

    /**
     * 
     * @Description: 根据表名删除序列
     * @author lianhanwen
     * @date 2017年5月26日 下午12:44:37
     * @param tableName
     */
    void deleteSeq(@Param("tableName") String tableName);

    /**
     * @Description: 根据表名创建id自增的触发器
     * @author lianhanwen
     * @date 2017年5月18日 下午10:24:31
     * @param tableName
     */
    void createTrigger(@Param("tableName") String tableName);

    /**
     * @Description: 拷贝表结构到目标表
     * @author lianhanwen
     * @date 2017年6月22日 下午5:27:29
     * @param sourceTableName
     * @param targetTableName
     */
    void copyTable(@Param("source") String sourceTableName, @Param("target") String targetTableName);

    /**
     * @Description: 增加列
     * @author lianhanwen
     * @date 2017年6月24日 下午4:56:05
     * @param tableName
     * @param columns
     */
    void addColumn(@Param("tableName") String tableName, @Param("columns") List<ColumnDef> columns);

    /**
     * @Description: 修改表
     * @author lianhanwen
     * @date 2017年6月24日 下午4:56:31
     * @param tableDef
     */
    void alterTable(TableDef tableDef);

}
