package com.jk.ndtetl.dbmeta.service.impl;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.BusinessType;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;
import com.jk.ndtetl.system.service.IUserService;

public class BusinessTypeServiceImplTest extends SpringTest {

    @Autowired
    private IBusinessTypeService businessTypeService;

    @Autowired
    private IUserService iUserService;

    @Test
    public void testSave() {
        // BusinessType businessType = new BusinessType();
        // businessType.setName("123");
        // businessTypeService.save(businessType);
        // User queryUser = iUserService.getByName("admin");
        iUserService.getByNameTest("admin");

    }

    @Test
    public void testDeleteById() {
        businessTypeService.deleteById(3);
    }

    @Test
    public void testUpdate() {
        BusinessType businessType = businessTypeService.getById(1);
        businessType.setName("林权信息");
        businessTypeService.update(businessType);
    }

    @Test
    public void testGetById() {
        fail("Not yet implemented");
    }

    @Test
    public void testListAll() {
        businessTypeService.listAll();
    }

    @Test
    public void testListByPage() {
        BasePage page = new BasePage();
        businessTypeService.listByPage(page);
    }

}
