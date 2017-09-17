package com.jk.ndtetl.etl.service.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.dbmeta.BusinessType;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IDbMetaService;
import com.jk.ndtetl.etl.service.IDDLExecutorService;

public class DDLExecutorServiceImplTest extends SpringTest {

    @Autowired
    private IDDLExecutorService ddlExecutorService;

    @Autowired
    private IDbMetaService dbMetaService;

    @Test
    public void testCreateTable() {
        TableDef tableDef=new TableDef();
        tableDef.setTableName("etl_cache_101");
        BusinessType businessType=new BusinessType();
        businessType.setEtlBusinessTypeId(2);
        tableDef.setBusinessType(businessType);
        List<ColumnDef> columns=new ArrayList<>();
        ColumnDef columnDef1=new ColumnDef();
        columnDef1.setColumnName("num1");
        columnDef1.setDataType(ColumnDef.COLUMN_TYPE_NUMBER);
        columnDef1.setFieldLength(10);
        ColumnDef columnDef2=new ColumnDef();
        columnDef2.setColumnName("num2");
        columnDef2.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef2.setFieldLength(50);
        ColumnDef columnDef3=new ColumnDef();
        columnDef3.setColumnName("a2");
        columnDef3.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef3.setFieldLength(50);
        ColumnDef columnDef4=new ColumnDef();
        columnDef4.setColumnName("a3");
        columnDef4.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef4.setFieldLength(50);
        ColumnDef columnDef5=new ColumnDef();
        columnDef5.setColumnName("a4");
        columnDef5.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef5.setFieldLength(50);
        ColumnDef columnDef6=new ColumnDef();
        columnDef6.setColumnName("a5");
        columnDef6.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef6.setFieldLength(50);
        ColumnDef columnDef7=new ColumnDef();
        columnDef7.setColumnName("a6");
        columnDef7.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef7.setFieldLength(50);
        ColumnDef columnDef8=new ColumnDef();
        columnDef8.setColumnName("a7");
        columnDef8.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef8.setFieldLength(50);
        ColumnDef columnDef9=new ColumnDef();
        columnDef9.setColumnName("a8");
        columnDef9.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef9.setFieldLength(50);
        ColumnDef columnDef10=new ColumnDef();
        columnDef10.setColumnName("a9");
        columnDef10.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef10.setFieldLength(50);
        ColumnDef columnDef11=new ColumnDef();
        columnDef11.setColumnName("a10");
        columnDef11.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef11.setFieldLength(50);
        ColumnDef columnDef12=new ColumnDef();
        columnDef12.setColumnName("a11");
        columnDef12.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef12.setFieldLength(50);
        ColumnDef columnDef13=new ColumnDef();
        columnDef13.setColumnName("a12");
        columnDef13.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef13.setFieldLength(50);
        columns.add(columnDef1);
        columns.add(columnDef2);
        columns.add(columnDef3);
        columns.add(columnDef4);
        columns.add(columnDef5);
        columns.add(columnDef6);
        columns.add(columnDef7);
        columns.add(columnDef8);
        columns.add(columnDef9);
        columns.add(columnDef10);
        columns.add(columnDef11);
        columns.add(columnDef12);
        columns.add(columnDef13);
        tableDef.setColumns(columns);
//        dbMetaService.saveTableDefAndColumns(tableDef);
//        ddlExecutorService.createTable(tableDef);
        dbMetaService.createTable(tableDef);
    }

    @Test
    public void testAddColumn() {
        List<ColumnDef> columns = new ArrayList<>();
        ColumnDef columnDef2 = new ColumnDef();
        columnDef2.setColumnName("email");
        columnDef2.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef2.setFieldLength(50);
        ColumnDef columnDef3 = new ColumnDef();
        columnDef3.setColumnName("updated");
        columnDef3.setDataType(ColumnDef.COLUMN_TYPE_TIMESTAMP);
        columnDef3.setFieldLength(8);
        columns.add(columnDef2);
        columns.add(columnDef3);
        ddlExecutorService.addColumn("demo1111", columns);
    }

    @Test
    public void testDeleteTable() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteSeq() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateSeq() {
        fail("Not yet implemented");
    }

}
