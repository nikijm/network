package com.jk.ndtetl.util.etl;/**
 * Created by polite on 2017/7/14.
 */

import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.util.Checker;

import java.util.ArrayList;
import java.util.List;

/**
 * 朱生
 *
 * @create 2017-07-14 10:46
 **/
public class DataCleanUtil {


    /**
     * 排除空字段对象,排除条件ColumnDef 主键空  如下： columns:[{}]
     * @param tableDefs
     * @return
     */
    public static Object excludeEmptyObject(List<TableDef> tableDefs) {
        if (Checker.isNotNullOrEmpty(tableDefs)) {
            for (TableDef tableDef:tableDefs) {
                if (Checker.isNotNullOrEmpty(tableDef.getColumns())) {
                    List<ColumnDef> columnDefList = new ArrayList<>();
                    for (ColumnDef columnDef:tableDef.getColumns()) {
                        if (null != columnDef.getEtlColumnId()) {
                            columnDefList.add(columnDef);
                        }
                    }
                    tableDef.setColumns(columnDefList);
                }
            }
        }

        return tableDefs;
    }

}
