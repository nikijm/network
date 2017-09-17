package com.jk.ndtetl.dbmeta.dao;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.dbmeta.TableDef;

/**
 * 
 * @ClassName: SheetLogDao
 * @Description: 数据日志相关的方法
 * @author lianhanwen
 * @date 2017年6月1日 上午8:58:57
 *
 */
public interface TableDefDao extends BaseDao<TableDef> {

    /**
     * 根据表明查询table信息
     * 
     * @author lianhanwen
     * @date 2017年7月4日 下午3:38:53
     * @param tableName
     * @return
     */
    TableDef getByTableName(String tableName);
    /**
     * 
     * @Description: 通过表id查规则
     * @author wangzhi
     * @date  2017年7月11日 下午3:20:05
     * @param tableId
     * @return
     * @return TableDef
     */
    TableDef getRuleByTableId(Integer tableId);

}
