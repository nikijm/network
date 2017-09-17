package com.jk.ndtetl.system.service;

import com.github.pagehelper.Page;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.system.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by 朱生 on 2017/5/13.
 */
public interface ICompareService {

    /**
     * @return
     * @Description: 查询比较两张表数据
     * @author fangwei
     * @date 2017年5月15日 下午2:11:05
     */
    List<Map<String, Object>> listCompareAll(@Param("tableName") String tableName1, @Param("tableName2") String tableName2,BasePage basePage);

}
