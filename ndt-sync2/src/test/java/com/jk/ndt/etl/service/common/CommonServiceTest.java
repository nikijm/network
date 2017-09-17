package com.jk.ndt.etl.service.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.entity.CommonQueryObj;

public class CommonServiceTest extends SpringTest {
    @Autowired
    private CommonService commonService;

    @Test
    public void queryTest() {
        String tableName = "ETL_MIRROR_BUSINESS";
        List<CommonQueryObj> queryList = new ArrayList<CommonQueryObj>();
        String colunmName = "name";
        String operator = "like";
        Object param = "王%";
        CommonQueryObj query = new CommonQueryObj(colunmName, operator, param);
        queryList.add(query);
        List<Map<String, Object>> listByQueryObj = commonService.listByQueryObj(tableName, queryList);
        System.out.println(listByQueryObj);

        query.setOperator("in");
        List<String> paramsList = new ArrayList<String>();
        paramsList.add("张三");
        paramsList.add("李四");
        query.setParam(paramsList);
        listByQueryObj = commonService.listByQueryObj(tableName, queryList);
        System.out.println(listByQueryObj);
    }

}
