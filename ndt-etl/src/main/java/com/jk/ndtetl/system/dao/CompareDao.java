package com.jk.ndtetl.system.dao;/**
 * Created by 朱生 on 2017/6/9.
 */

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 朱生
 *
 * @create 2017-06-09 14:25
 **/
public interface CompareDao {

    /**
     * @return
     * @Description: 查询比较两张表数据
     * @author fangwei
     * @date 2017年5月15日 下午2:11:05
     */
    List<Map<String, Object>> listCompareAll(@Param("tableName1") String tableName1, @Param("tableName2") String tableName2);


}
