package com.jk.ndtetl.dbmeta.service;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;

/**
 * @ClassName: IColumnDefService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lianhanwen
 * @date 2017年6月22日 下午4:58:14
 *
 */
public interface IColumnDefService extends IBaseService<ColumnDef> {

    /**
     * 批量保存column
     * 
     * @author lianhanwen
     * @date 2017年7月6日 下午4:19:42
     * @param columns
     */

    void saveColumns(TableDef tableDef);

}
