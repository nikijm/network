package com.jk.ndt.etl.dao.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.entity.CommonQueryObj;
import com.jk.ndt.etl.entity.Page;

/**
 * 
 * @ClassName: CommonDao
 * @Description: 公共mapper接口，主要用于动态创建表，查询动态表中的数据
 * @author fangwei
 * @date 2017年5月15日 下午1:56:12
 *
 */
public interface CommonDao {

    /**
     * 
     * @Description: 根据解析出来的内容创建表
     * @author lianhanwen
     * @date 2017年5月18日 下午10:19:08
     * @param dataTable
     */
    void createTable(DataTable dataTable);

    /**
     * 
     * @Description: 根据表名删除表
     * @author lianhanwen
     * @date 2017年5月26日 下午12:44:17
     * @param tableName
     */
    void deleteTable(@Param("tableName") String tableName);

    /**
     * 
     * @Description: 根据表名创建序列
     * @author lianhanwen
     * @date 2017年5月18日 下午10:19:42
     * @param dataTable
     */
    void createSeq(@Param("tableName") String tableName);

    /**
     * 
     * @Description: 根据表名删除序列
     * @author lianhanwen
     * @date 2017年5月26日 下午12:44:37
     * @param tableName
     */
    void deleteSeq(@Param("tableName") String tableName);

    /**
     * @Description: 根据表名创建id自增的触发器
     * @author lianhanwen
     * @date 2017年5月18日 下午10:24:31
     * @param tableName
     */
    void createTrigger(@Param("tableName") String tableName);

    /**
     * 
     * @Description: 查询制定表所有的数据
     * @author fangwei
     * @date 2017年5月15日 下午2:11:05
     * @return
     */
    List<Map<String, Object>> listAll(@Param("tableName") String tableName, @Param("unquieId") String unquieId);

    /**
     * 
     * @Description: 将dataTable中的数据保存到数据库表中
     * @author fangwei
     * @date 2017年5月15日 下午2:17:06
     * @param dataTable
     * @return
     */
    void saveTableContent(DataTable dataTable);

    /**
     * 
     * @Description: 保存带有id的表数据
     * @author fangwei
     * @date 2017年6月7日 下午4:29:08
     * @param dataTable
     */
    void saveTableContentWithId(DataTable dataTable);

    void saveContent(DataTable dataTable);

    /**
     * 
     * @Description: 带查询条件的查询
     * @author lianhanwen
     * @date 2017年5月27日 上午9:44:45
     * @param tableName
     * @param unquieId
     * @param page
     * @return
     */
    List<Map<String, Object>> listByPage(@Param("tableName") String tableName, @Param("unquieId") String unquieId,
            @Param("page") Page page);

    /**
     * 
     * @Description: 根据查询对象查询数据
     * @author fangwei
     * @date 2017年6月5日 下午2:28:08
     * @param tableName
     * @param queryList
     * @return
     */
    List<Map<String, Object>> listByQueryObj(@Param("tableName") String tableName,
            @Param("queryList") List<CommonQueryObj> queryList);

    /**
     * 
     * @Description: 从多个表查询数据
     * @author lianhanwen
     * @date 2017年5月27日 上午11:25:44
     * @param tables
     * @param page
     * @return
     */
    List<Map<String, Object>> listFromTables(@Param("tables") List<String> tables, @Param("page") Page page);

    /**
     * 
     * @Description: 根据多个或一个id的查询
     * @author lianhanwen
     * @date 2017年5月27日 下午12:37:00
     * @param tables
     * @param ids
     * @return
     */
    List<Map<String, Object>> listByIds(@Param("tableName") String tableName, @Param("unquieId") String unquieId,
            @Param("ids") List<Integer> ids);

}
