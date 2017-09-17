package com.jk.ndtetl.dbmeta;

import java.util.List;

/**
 * @ClassName: DbMeta
 * @Description: 封装表和列的实体类
 * @author lianhanwen
 * @date 2017年6月22日 下午3:10:01
 *
 */
// @JsonInclude(value = Include.NON_NULL)
public class DbMeta {
    /**
     * 表信息
     */
    private TableDef tableDef;
    /**
     * 列信息
     */
    private List<ColumnDef> columns;

    public TableDef getTableDef() {
        return tableDef;
    }

    public void setTableDef(TableDef tableDef) {
        this.tableDef = tableDef;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

}