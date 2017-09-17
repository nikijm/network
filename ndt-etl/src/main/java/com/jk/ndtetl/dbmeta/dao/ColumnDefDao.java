package com.jk.ndtetl.dbmeta.dao;

import java.util.List;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.dbmeta.ColumnDef;

/**
 * @ClassName: ColumnDefDao
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lianhanwen
 * @date 2017年6月22日 下午3:09:37
 *
 */
public interface ColumnDefDao extends BaseDao<ColumnDef> {

    /**
     * 根据tableId删除
     * 
     * @author lianhanwen
     * @date 2017年7月18日 下午3:02:45
     * @param id
     */
    void deleteByTableId(Integer id);

    /**
     * 根据tableId查询
     * 
     * @author lianhanwen
     * @date 2017年7月18日 下午3:02:45
     * @param id
     */
    List<ColumnDef> getByTableId(Integer id);
}
