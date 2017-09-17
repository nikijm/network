package com.jk.ndtetl.etl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jk.ndtetl.CommonQueryObj;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.etl.BaseDataEntity;
import com.jk.ndtetl.etl.DataTable;

/**
 * 
 * @ClassName: CommonDao
 * @Description: 公共mapper接口，查询动态表中的数据
 * @author fangwei
 * @date 2017年5月15日 下午1:56:12
 *
 */
public interface CommonDao {

    /**
     * @Description: 查询指定表所有的数据
     * @author lianhanwen
     * @date 2017年6月22日 下午5:31:49
     * @param tableName
     * @param unquieId
     * @return
     */
    List<Map<String, Object>> listAll(@Param("tableName") String tableName, @Param("unquieId") String unquieId);

    /**
     * @Description: 将dataTable中的数据保存到数据库表中
     * @author lianhanwen
     * @date 2017年6月22日 下午5:32:08
     * @param dataTable
     */
    void saveCacheTableContent(DataTable dataTable);

    /**
     * 修改数据
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午9:07:38
     * @param dataTable
     */
    void updateDataById(BaseDataEntity baseDataEntity);
    /**
     * 根据文件的unId批量修改状态
     * @author lianhanwen
     * @date 2017年7月27日 上午11:19:47 
     * @param baseDataEntity
     */
    void updateDataByUnId(BaseDataEntity baseDataEntity);

    /**
     * 批量修改
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午10:38:10
     * @param baseDataEntity
     * @param ids
     */
    void updateDataByIds(@Param("baseDataEntity") BaseDataEntity baseDataEntity, @Param("ids") List<Integer> ids);

    /**
     * @Description: 保存带有id的表数据
     * @author lianhanwen
     * @date 2017年6月22日 下午5:32:21
     * @param dataTable
     */
    void saveTableContentWithId(DataTable dataTable);

    /**
     * @Description: 带查询条件的查询
     * @author lianhanwen
     * @date 2017年6月22日 下午5:32:35
     * @param tableName
     * @param unquieId
     * @param page
     * @return
     */
    List<Map<String, Object>> listByPage(@Param("tableName") String tableName,
            @Param("isSearchColumns") List<String> isSearchColumns, @Param("page") BasePage page);

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
    List<Map<String, Object>> listFromTables(@Param("tables") List<String> tables, @Param("page") BasePage page);

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

    /**
     * @Description: 判断列名是否重复
     * @author lianhanwen
     * @date 2017年6月24日 下午4:54:48
     * @param tableName
     * @param newColumnName
     * @return
     */
    Integer isNewColumnNameAvailable(@Param("tableName") String tableName, @Param("columnName") String newColumnName);

    /**
     * @Description: 判断表名是否重复
     * @author lianhanwen
     * @date 2017年6月24日 下午4:55:15
     * @param newTableName
     * @return
     */
    Integer isNewTableNameAvailable(@Param("tableName") String newTableName);

    /**
     * 根据表名获得列名
     * 
     * @author lianhanwen
     * @date 2017年7月12日 下午6:20:49
     * @param tableName
     * @return
     */
    List<String> getColumnNames(@Param("tableName") String tableName);

    /**
     * 根据unid删除数据
     * 
     * @author lianhanwen
     * @date 2017年7月14日 下午2:52:37
     * @param unId
     */
    void deleteByUnId(@Param("tableName") String tableName, @Param("unId") String unId);

    /**
     * 测试用过
     * 
     * @author lianhanwen
     * @date 2017年7月21日 上午10:56:52
     * @param dataTable
     * @param unUseColumnNums
     * @return
     */
    List<Map<String, Object>> asda(@Param("dataTable") DataTable dataTable,
            @Param("unUseColumnNums") List<Integer> unUseColumnNums);

    /**
     * 将dataTable中的数据保存到数据库表中的公共方法
     * 
     * @author lianhanwen
     * @date 2017年7月21日 上午10:56:56
     * @param dataTable
     */
    void saveTableContent(DataTable dataTable);

    /**
     * 查询条数
     * 
     * @author lianhanwen
     * @date 2017年7月21日 下午3:01:15
     * @param tableName
     * @param unId
     * @param result
     * @return
     */
    int getCountByUnIdAndResult(@Param("tableName") String tableName, @Param("unId") String unId,
            @Param("result") Integer result);

    /**
     * 查询对比数据
     * 
     * @author lianhanwen
     * @date 2017年7月24日 下午3:45:14
     * @param cacheTableName
     * @param cleanTableName
     * @return
     */
    List<Map<String, Object>> getContrastDatas(@Param("sourceTableName") String cacheTableName,
            @Param("targetTableName") String cleanTableName, @Param("unId") String unId,
            @Param("sourceColumnNames") List<String> cacheColumnNames,
            @Param("targetColumnNames") List<String> cleanColumnNames);

}
