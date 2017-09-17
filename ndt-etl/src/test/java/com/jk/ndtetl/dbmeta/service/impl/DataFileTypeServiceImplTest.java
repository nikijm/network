package com.jk.ndtetl.dbmeta.service.impl;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.dao.DataFileTypeDao;

public class DataFileTypeServiceImplTest extends SpringTest {
    @Autowired
    private DataFileTypeDao dataFileTypeDao;

    @Test
    public void testSave() {
        DataFileType dataFiletype = new DataFileType();
        dataFiletype.setDescription("测试2");
        dataFiletype.setName("测试dataFiletype");
        dataFileTypeDao.save(dataFiletype);
    }

    @Test
    public void testDeleteById() {
        dataFileTypeDao.deleteById(2);
    }

    @Test
    public void testUpdate() {
        DataFileType dataFileType = dataFileTypeDao.getById(1);
        dataFileType.setName("update测试");

        dataFileTypeDao.update(dataFileType);
    }

    @Test
    public void testGetById() {
        fail("Not yet implemented");
    }

    @Test
    public void testListAll() {
        List<DataFileType> listAll = dataFileTypeDao.listAll();
        System.out.println(listAll);
    }

    @Test
    public void testListByPage() {
        BasePage page = new BasePage();
        List<DataFileType> listByPage = dataFileTypeDao.listByPage(page);
        System.out.println(listByPage);
    }

}
