package com.jk.ndt.etl.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.dao.common.CommonDao;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.service.common.CommonService;

public class CommonMapperTest extends SpringTest {
    @Autowired
    private CommonDao commonMapper;
    @Autowired
    private CommonService commonService;

    @Test
    public void testName() throws Exception {
        Integer[] ids = new Integer[] { 1, 2, 3, 45 };
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        String jsonString = JSON.toJSONString(map);
        System.out.println(jsonString);
    }

    @Test
    public void testCreateTable() {
        DataTable dataTable = new DataTable();
        dataTable.setTableName("DEMO4");
        List<String> header = new ArrayList<>();
        header.addAll(Arrays.asList(new String[] { "name", "age" }));
        dataTable.setHeader(header);
        List<List<String>> content = new ArrayList<>();
        content.add(Arrays.asList(new String[] { "xiao", "12" }));
        content.add(Arrays.asList(new String[] { "dada", "13" }));
        dataTable.setContent(content);
        commonService.createTable(dataTable);
        commonMapper.createSeq(dataTable.getTableName());
    }

    //
    @Test
    public void testDelete() {
        commonService.deleteTable("demo4");
        commonService.deleteSeq("demo4");
    }

    @Test
    public void testListAll() {
        // List<Map<String, Object>> listByIds =
        // commonMapper.listByIds("ETL_ORIGINAL_86", null,Arrays.asList(new
        // Integer[] {1}));
        // System.out.println(listByIds);
        Page page = new Page();

        List<Map<String, Object>> listByPage = commonMapper.listByPage("ETL_ORIGINAL_206", null, page);
        System.out.println(listByPage);
    }

    //
    @Test
    public void testSaveTableContent() {
        DataTable dataTable = new DataTable();
        dataTable.setTableName("DEMO4");
        List<String> header = new ArrayList<>();
        header.addAll(Arrays.asList(new String[] { "name", "age" }));
        dataTable.setHeader(header);
        List<List<String>> content = new ArrayList<>();
        content.add(Arrays.asList(new String[] { "", "" }));
        content.add(Arrays.asList(new String[] { "dada", "13" }));
        dataTable.setContent(content);
        commonMapper.saveTableContent(dataTable);

    }

}
