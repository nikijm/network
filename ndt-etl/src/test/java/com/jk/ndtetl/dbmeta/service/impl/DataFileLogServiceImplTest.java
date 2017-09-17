package com.jk.ndtetl.dbmeta.service.impl;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileLog;
import com.jk.ndtetl.dbmeta.service.IDataFileLogService;

public class DataFileLogServiceImplTest extends SpringTest {

    @Autowired
    private IDataFileLogService dataFileLogService;

    @Test
    public void testSave() {
        DataFileLog dataFileLog = new DataFileLog();
        dataFileLog.setAction(DataFile.ACTION_CLEAN);
        dataFileLog.setTotalNum(1000);
        dataFileLogService.save(dataFileLog);
    }

    @Test
    public void testDeleteById() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetById() {
        dataFileLogService.getById(1);
    }

    @Test
    public void testListAll() {
        dataFileLogService.listAll();
    }

    @Test
    public void testListByPage() {
        BasePage page=new BasePage();
        dataFileLogService.listByPage(page);
    }

}
