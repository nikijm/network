package com.jk.ndt.etl.service.logs;

import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.service.BaseService;

public interface SheetLogService extends BaseService<SheetLog>{
    /**
     * 
     * @Description:根据sheetId删除缓存日志,仅供手动回滚使用
     * @author lianhanwen
     * @date 2017年6月6日 下午4:38:49 
     * @param target
     */
    void deleteCacheLogBySheetId(Integer sheetId);
}
