package com.jk.ndtetl.dbmeta.service.impl;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.service.IColumnDefService;

public class ColumnDefServiceImplTest extends SpringTest {
    @Autowired
    private IColumnDefService columnDefService;

    @Test
    public void testSave() {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setColumnName("id");
        columnDef.setDataType(ColumnDef.COLUMN_TYPE_NUMBER);
        columnDef.setFieldLength(2000);
        columnDef.setDescription("测试");
        columnDef.setIsActive('Y');
        columnDef.setName("测试id");
        columnDefService.save(columnDef);
        columnDef.setColumnName("name");
        columnDef.setDataType(ColumnDef.COLUMN_TYPE_NVARCHAR2);
        columnDef.setFieldLength(2000);
        columnDef.setDescription("测试");
        columnDef.setIsActive('Y');
        columnDef.setName("测试name");
        columnDefService.save(columnDef);
    }

    @Test
    public void testDeleteById() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdate() {
        ColumnDef columnDef = columnDefService.getById(1);
        columnDef.setDescription("update");
        columnDefService.update(columnDef);
    }

    @Test
    public void testGetById() {
        fail("Not yet implemented");
    }

    @Test
    public void testListAll() {
        List<ColumnDef> listAll = columnDefService.listAll();
        System.out.println(listAll);
    }

    @Test
    public void testListByPage() {
        BasePage page=new BasePage();
        List<ColumnDef> listAll = columnDefService.listByPage(page);
        System.out.println(listAll);
    }

}
