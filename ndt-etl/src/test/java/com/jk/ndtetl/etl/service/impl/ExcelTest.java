package com.jk.ndtetl.etl.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.ndtetl.SpringTest;
import com.jk.ndtetl.dbmeta.service.IDataFileTypeService;
import com.jk.ndtetl.etl.DataTable;

public class ExcelTest extends SpringTest {

    @Autowired
    private IDataFileTypeService dataFileTypeService;

    @Test
    public void testExcel() throws Exception {
        try {

            ExcelConverter eu = new ExcelConverter();
            // System.out.println("=======测试Excel 默认 读取========");
            // eu.readExcel("d:\\test.xls");
            //
            // System.out.println("\n=======测试Excel 从第四行读取，倒数第二行结束========");
            // eu = eu.RestoreSettings();// 还原设定
            // eu.setStartReadPos(3);
            // eu.setEndReadPos(-1);
            // eu.readExcel("d:\\test.xls");
            //
            // System.out.println("\n=======测试Excel 读取第二个sheet========");
            // eu = eu.RestoreSettings();// 还原设定
            // eu.setSelectedSheetIdx(1);
            // eu.readExcel("d:\\test.xls");

            // System.out.println("\n=======测试Excel 读取所有的sheet========");
            // eu.setOnlyReadOneSheet(false);
            List<DataTable> dataTable = eu.readExcel("C:\\Users\\lianhanwen\\Desktop\\彭州市新兴镇专业大户2017.xlsx");
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(dataTable);
            System.out.println(json);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testName() throws Exception {
        String fieldsAlign = dataFileTypeService.getById(2).getFieldsAlign();
        Map<String, String> fieldsAlignMap = JSON.parseObject(fieldsAlign, Map.class);
        List<String> header = new ArrayList<String>();
        // header.addAll(java.util.Arrays.asList(new
        // String[]{"1","2","3","4","5","6","7","8","9"}));
        for (int i = 0; i < header.size(); i++) {
            header.set(i, fieldsAlignMap.get(i + "") == null ? i + "" : fieldsAlignMap.get(i + ""));
        }
        System.out.println(header);
    }

    @Test
    public void testSubString() throws Exception {
        // String substring = "填报单位:敖平镇".substring(1, 3);
        // System.out.println(substring);
        // System.out.println(new File("/E:/Text.txt").createNewFile());
        // Map<String, Object> map=new HashMap<>();
        // int contentNum = (int) map.get("contentNum");
        // System.out.println(contentNum);
        String path = "C:\\Users\\lianhanwen\\Desktop\\upload";
        File file = new File(path);
        File[] tempList = file.listFiles();
        System.out.println("该目录下对象个数：" + tempList.length);
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                System.out.println("文     件：" + tempList[i]);
            }
            if (tempList[i].isDirectory()) {
                System.out.println("文件夹：" + tempList[i]);
            }
        }

    }

}
