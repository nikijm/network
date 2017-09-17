package com.jk.ndtetl.util.etl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.util.Checker;

public class TransformerUtil {

    public static Map<String, Object> transColumnName2Name(Map<String, Object> data, List<ColumnDef> columns) {
        Map<String, Object> newMap = new LinkedHashMap<>();
        if (Checker.isNotNullOrEmpty(columns)) {
            for (ColumnDef columnDef : columns) {
                String columnName = columnDef.getColumnName();
                String name = columnDef.getName();
                Object value = data.get(columnName);
                newMap.put(name == null || "".equals(name) ? columnName : name, value);
            }
        }
        return newMap;
    }

    /**
     * 数据header排序,缓存、转换、清洗、校验列表用到
     * 
     * @param columnDefs
     * 字段列表
     * @return
     */
    public static Map<String, Object> getHeader(List<ColumnDef> columns) {
        Collections.sort(columns);
        Map<String, Object> map = new LinkedHashMap<>();
        if (Checker.isNotNullOrEmpty(columns)) {
            for (ColumnDef columnDef : columns) {
                String columnName = columnDef.getColumnName();
                String name = columnDef.getName();
                map.put(columnName, name == null || "".equals(name) ? columnName : name);
            }
        }
        return map;
    }

    public static Map<String, Object> getContrastHeader(List<ColumnDef> sourceColumns, List<ColumnDef> targetColumns) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (Checker.isNotNullOrEmpty(sourceColumns)) {
            for (ColumnDef columnDef : sourceColumns) {
                String columnName = columnDef.getColumnName();
                String name = columnDef.getName();
                map.put(("source" + columnName).toUpperCase(), name == null || "".equals(name) ? columnName : name);
            }
        }
        if (Checker.isNotNullOrEmpty(targetColumns)) {
            for (ColumnDef columnDef : targetColumns) {
                String columnName = columnDef.getColumnName();
                String name = columnDef.getName();
                map.put(("target" + columnName).toUpperCase(), name == null || "".equals(name) ? columnName : name);
            }
        }
        return map;
    }

}
