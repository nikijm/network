package com.jk.ndtetl.dbmeta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IDbMetaService;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.exception.CustomException;

public class DbMetaServiceImplTest extends SpringTest {
    @Autowired
    private IDbMetaService dbMetaService;
    @Autowired
    private CommonDao commonDao;

    @Test
    public void testQueryDbMetaByETLFileTypeId() {
        Map<?, ?> map = JSON.parseObject(null, Map.class);
        System.out.println(map);
    }

    @Test
    public void testQueryDbMetaByTableName() {
         TableDef tableDef = dbMetaService.queryDbMetaByTableName("demo1");
         System.out.println(tableDef);
    }
    @Test
    public void testGetContrastHeader() throws CustomException {
        Map<String, Object> contrastHeader = null;
        try {
            contrastHeader = dbMetaService.getContrastHeader(13, "clean");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(contrastHeader));
    }
    @Test
    public void testGetContrastDatas() {
        List<String> columnNames1 = commonDao.getColumnNames("ETL_CACHE_DISCREDITABLE");
        List<String> columnNames2 = commonDao.getColumnNames("ETL_CACHE_DIS");
        List<Map<String, Object>> contrastDatas = commonDao.getContrastDatas("ETL_CACHE_DISCREDITABLE", "ETL_CACHE_DIS", "b0507031-e85c-4f8d-830c-84b35f1b8c9c",columnNames1,columnNames2);
        System.out.println(JSON.toJSONString(contrastDatas));
    }
    @Test
    public void testSaveTableDefAndColumns() {
        TableDef tableDef=new TableDef();
        tableDef.setTableName("demo11112");
        List<ColumnDef> columns=new ArrayList<>();
        ColumnDef columnDef1=new ColumnDef();
        columnDef1.setColumnName("age");
        columnDef1.setTableDef(tableDef);
        columnDef1.setDataType(ColumnDef.COLUMN_TYPE_NUMBER);
        columnDef1.setFieldLength(5);
        ColumnDef columnDef2=new ColumnDef();
        columnDef2.setColumnName("name");
        columnDef2.setTableDef(tableDef);
        columnDef2.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef2.setFieldLength(50);
        ColumnDef columnDef3=new ColumnDef();
        columnDef3.setColumnName("created");
        columnDef3.setTableDef(tableDef);
        columnDef3.setDataType(ColumnDef.COLUMN_TYPE_TIMESTAMP);
        columnDef3.setFieldLength(8);
        columns.add(columnDef1);
        columns.add(columnDef2);
        columns.add(columnDef3);
        tableDef.setColumns(columns);
        dbMetaService.saveTableDefAndColumns(tableDef);
    }

}
