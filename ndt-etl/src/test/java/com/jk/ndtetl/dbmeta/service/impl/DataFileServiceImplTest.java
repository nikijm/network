package com.jk.ndtetl.dbmeta.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.util.QueryParam;

public class DataFileServiceImplTest extends SpringTest {

    @Autowired
    private IDataFileService dataFileService;

    @Test
    public void testSave() {
        DataFile dataFile = new DataFile();
        dataFile.setFileName("test");
        dataFile.setMessage("测试");
        dataFile.setAction(DataFile.ACTION_CACHE);
        dataFileService.save(dataFile);
    }

    @Test
    public void testDeleteById() {
        DataFile dataFile = new DataFile();
        List<DataFile> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dataFile.setUnId(i + "");
            list.add(dataFile);
        }
        System.out.println(list);
    }

    @Test
    public void testUpdate() {
        DataFile dataFile = dataFileService.getById(1);
        dataFile.setFileName("测试update");
        dataFile.setSha1("sha1测试");
        dataFileService.update(dataFile);
    }

    @Test
    public void testGetById() {
        DataFile dataFile = dataFileService.getById(1);
        System.out.println(dataFile);
    }

    @Test
    public void testListAll() {
        List<DataFile> listAll = dataFileService.listAll();
        System.out.println(listAll);
    }

    @Test
    public void testListByPage() {
        BasePage page = new BasePage();
        dataFileService.listByPage(page);
    }

    @Test
    public void testGetFileNumFromTodayByUserId() {
        int numFromOneDayByUser = dataFileService.getFileNumFromTodayByUserId(1);
        System.out.println(numFromOneDayByUser);
    }

    @Test
    public void testAnalyzeAndCache() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("0", "num1");
        map.put("1", "num2");
        map.put("2", "A2");
        map.put("3", "A3");
        map.put("4", "A4");
        map.put("5", "A5");
        map.put("6", "A6");
        map.put("7", "A7");
        map.put("8", "A8");
        map.put("9", "A9");
        map.put("10", "A10");
        map.put("11", "A11");
        map.put("12", "A12");
        System.out.println(JSON.toJSONString(map));

        // dataFileService.analyzeAndCache(id, user,realPath);
    }

    @Test
    public void testListClean() {
        QueryParam queryParam = new QueryParam();
        queryParam.getParam().put("statusClean", DataFile.DATA_STATUS_READY);
        List<DataFile> list = dataFileService.listFileToProcess(queryParam);
        for (DataFile dataFile : list) {
            System.out.println(dataFile.getDataFileType().getTableCleanId());
            System.out.println(dataFile.getDataFileType().getTableCacheId());
            System.out.println(dataFile.getDataFileType().getTableConvertId());
            System.out.println(dataFile.getDataFileType().getTableValidateId());
        }
    }
    @Test
    public void testRollback() throws CustomException {
        dataFileService.rollBack(1);
    }

}
