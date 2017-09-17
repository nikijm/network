package com.jk.ndt.etl.converter.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.ndt.etl.converter.model.DataTable;

public class excelTest {

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

//			System.out.println("\n=======测试Excel 读取所有的sheet========");
//			eu.setOnlyReadOneSheet(false);
			List<DataTable> dataTable = eu.readExcel("C:\\Users\\Administrator\\Desktop\\无规则数据缓存测试.xlsx");
			ObjectMapper om = new ObjectMapper();
			String json = om.writeValueAsString(dataTable);
			System.out.println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
    public void testName() throws Exception {
//        ArrayList<Object> arrayList = new ArrayList<>(2);
//        String[] strs=new String[10];
//        
//        arrayList.add("1");
//        arrayList.add("1");
//        arrayList.add("1");
//        arrayList.add("1");
//        System.out.println(arrayList);
//        System.out.println(Arrays.asList(strs).toString());
	    Map<String, Boolean> map=new HashMap<>();
	    map.put("ha", true);
	    map.put("ha", true);
	    map.put("ha", false);
	        
	    System.out.println(UUID.randomUUID().toString());
	    System.out.println(map.get("ha"));
	    
    }

	
}
