package com.jk.ndtetl.etl.service.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.BaseDataEntity;
import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.etl.service.ICommonService;

public class CommonServiceImplTest extends SpringTest {

    @Autowired
    private ICommonService commonService;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private ITableDefService tableDefService;

    @Test
    public void testListAll() {
        DataTable dataTable = new DataTable();
        List<String> header = new ArrayList<>();
        header.addAll(java.util.Arrays.asList(new String[] { "1按时多", "2多", "3asd", "4asd多", "5asd打算" }));
        dataTable.setHeader(header);
        List<Integer> unUseColumnNums = new ArrayList<>();
        unUseColumnNums.add(1);
        unUseColumnNums.add(2);
        commonDao.asda(dataTable, unUseColumnNums);
    }

    @Test
    public void testListByPage() {
        TableDef tableDef = tableDefService.getById(47);
        BasePage page = new BasePage();
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", "陈正");
        page.setParam(param);
        List<Map<String, Object>> listByPage = commonService.listByPage(tableDef, page);
        System.out.println(listByPage);
    }

    @Test
    public void testListByIds() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateDatas() {
        List<Integer> ids = java.util.Arrays.asList(new Integer[] { 1, 2 });
        BaseDataEntity baseDataEntity = new BaseDataEntity();
        baseDataEntity.setTableName("test_5");
        baseDataEntity.setReason("aasdas");
        commonDao.updateDataByIds(baseDataEntity, ids);
    }

    @Test
    public void testSaveTableContent() {
    }

    @Test
    public void testListByQueryObj() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsNewTableNameAvailable() {
        Integer num = commonDao.isNewTableNameAvailable("demo1111");
        System.out.println(num);
    }

    @Test
    public void testIsNewColumnNameAvailable() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetHeader() {
        // Map<String, Object> header = commonService.getHeader("ETL_CACHE_39");
        // System.out.println(JSON.toJSONString(header));
    }

    @Test
    public void testGetCountByUnIdAndResult() {
        int countByUnIdAndResult = commonService.getCountByUnIdAndResult("ETL_CACHE_BIGFARM",
                "a5c432b3-f8f8-47c5-a725-98cbdbd9727e", 0);
        System.out.println(countByUnIdAndResult);
    }

}
