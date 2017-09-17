package com.jk.ndt.etl.dao.logs;

import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.entity.logs.SheetLog;
/**
 * 
 * @ClassName: SheetLogDao 
 * @Description: 数据日志相关的方法
 * @author lianhanwen 
 * @date 2017年6月1日 上午8:58:57 
 *
 */
public interface SheetLogDao extends BaseDao<SheetLog> {
    /**
     * 
     * @Description: 根据sheetId删除缓存日志,仅供手动回滚使用
     * @author lianhanwen
     * @date 2017年6月6日 下午4:39:25 
     * @param target
     */
    void deleteCacheLogBySheetId(Integer sheetId);
}
