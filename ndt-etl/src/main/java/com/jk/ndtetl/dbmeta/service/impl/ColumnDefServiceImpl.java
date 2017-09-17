package com.jk.ndtetl.dbmeta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.dao.ColumnDefDao;
import com.jk.ndtetl.dbmeta.service.IColumnDefService;
/**
 * @ClassName: ColumnDefServiceImpl 
 * @Description: ColumnDef实现类
 * @author lianhanwen 
 * @date 2017年6月22日 下午5:02:58 
 *
 */
@Service("columnDefService")
public class ColumnDefServiceImpl extends BaseServiceImpl<ColumnDef> implements IColumnDefService {
    @Autowired
    private ColumnDefDao columnDefDao;

    @Override
    protected BaseDao<ColumnDef> getDao() {
        return columnDefDao;
    }
    @Override
    public void saveColumns(TableDef tableDef) {
        TableDef t = new TableDef();
        t.setEtlTableId(tableDef.getEtlTableId());
        for (ColumnDef columnDef : tableDef.getColumns()) {
            columnDef.setColumnName(columnDef.getColumnName().toUpperCase());
            columnDef.setTableDef(t);
            columnDefDao.save(columnDef);
        }
    }

}
