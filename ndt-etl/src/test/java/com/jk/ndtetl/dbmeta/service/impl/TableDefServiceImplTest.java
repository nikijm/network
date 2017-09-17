package com.jk.ndtetl.dbmeta.service.impl;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.BusinessType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.ITableDefService;

public class TableDefServiceImplTest extends SpringTest {
    @Autowired
    private ITableDefService tableDefService;

    @Test
    public void testSave() {
        TableDef tableDef = new TableDef();
        tableDef.setDescription("单元测试");
        tableDef.setIsActive('Y');
        tableDef.setCategory("cache");
        BusinessType businessType=new BusinessType();
        businessType.setEtlBusinessTypeId(3);
        tableDef.setBusinessType(businessType);
        tableDef.setName("测试2");
        tableDef.setTableName("demo2");
        tableDefService.save(tableDef);
        System.out.println(tableDef.getEtlTableId());
        tableDef.setTableName("demo3");
        tableDef.setCategory("clean");
        tableDefService.save(tableDef);
        System.out.println(tableDef.getEtlTableId());
    }

    @Test
    public void testDeleteById() {
        tableDefService.deleteById(4);
    }

    @Test
    public void testUpdate() {
        TableDef tableDef = tableDefService.getById(41);
        tableDef.setDescription("update");
        tableDefService.update(tableDef);
    }

    @Test
    public void testGetById() {
        fail("Not yet implemented");
    }
    @Test
    public void testGetDataByOrgAndBusinessType() {
        BasePage page=new BasePage();
        tableDefService.getDataByOrgAndBusinessType("cache", 31, 1, page);
    }

    @Test
    public void testListAll() {
        List<TableDef> listAll = tableDefService.listAll();
        System.out.println(listAll);
    }

    @Test
    public void testListByPage() {
        BasePage page=new BasePage();
        List<TableDef> listByPage = tableDefService.listByPage(page);
        System.out.println(listByPage);
    }

}
