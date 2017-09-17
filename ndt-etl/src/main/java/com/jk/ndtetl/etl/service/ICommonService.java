package com.jk.ndtetl.etl.service;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.CommonQueryObj;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.etl.BaseDataEntity;
import com.jk.ndtetl.etl.DataTable;

/**
 * 
 * @ClassName: CommonService
 * @Description: 公共操作服务类
 * @author fangwei
 * @date 2017年5月16日 下午3:56:53
 *
 */
public interface ICommonService {

    /**
     * 
     * @Description: 查询制定表所有的数据
     * @author lianhanwen
     * @date 2017年5月18日 下午2:44:57
     * @param tableName
     * @param unquieId
     * @return
     */
    List<Map<String, Object>> listAll(String tableName, String unquieId);

    /**
     * 
     * @Description: 将dataTable中的数据保存到数据库缓存表中
     * @author lianhanwen
     * @date 2017年5月18日 下午2:45:28
     * @param dataTable
     * @return
     */
    void saveCacheTableContent(DataTable dataTable);

    /**
     * 将dataTable中的数据保存到数据库缓存表中的公共方法
     * 
     * @author lianhanwen
     * @date 2017年7月21日 上午10:55:28
     * @param dataTable
     */
    void saveTableContent(DataTable dataTable);

    /**
     * @Description: 根据分页条件查询数据
     * @author lianhanwen
     * @date 2017年6月13日 下午6:43:13
     * @param tableName
     * @param unquieId
     * @param page
     * @return
     */
    List<Map<String, Object>> listByPage(TableDef tableDef, BasePage page);

    /**
     * 
     * @Description: 根据查询对象查询数据
     * @author fangwei
     * @date 2017年6月5日 下午2:29:44
     * @param tableName
     * @param queryList
     * @return
     */
    List<Map<String, Object>> listByQueryObj(String tableName, List<CommonQueryObj> queryList);

    /**
     * @Description: 根据多个id查询数据列表
     * @author lianhanwen
     * @date 2017年6月13日 下午6:42:42
     * @param tableName
     * @param unquieId
     * @param ids
     * @return
     */
    List<Map<String, Object>> listByIds(String tableName, String unquieId, List<Integer> ids);

    /**
     * @Description: 判断列名是否重复
     * @author lianhanwen
     * @date 2017年6月24日 下午4:57:42
     * @param tableName
     * @param NewColumnName
     * @return
     */
    Boolean isNewColumnNameAvailable(String tableName, String NewColumnName);

    /**
     * @Description: 判断表名是否重复
     * @author lianhanwen
     * @date 2017年6月24日 下午4:57:59
     * @param newTableName
     * @return
     */
    Boolean isNewTableNameAvailable(String newTableName);

    /**
     * 修改数据
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午9:09:43
     * @param baseDataEntity
     */
    void updateDataById(BaseDataEntity baseDataEntity);

    /**
     * 批量修改
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午10:38:31
     * @param baseDataEntity
     * @param ids
     */
    void updateDataByIds(BaseDataEntity baseDataEntity, List<Integer> ids);

    /**
     * 根据表名获得列名
     * 
     * @author lianhanwen
     * @date 2017年7月12日 下午6:22:53
     * @param tableName
     * @return
     */
    List<String> getColumnNames(String tableName);

    /**
     * 根据unId删除
     * 
     * @author lianhanwen
     * @date 2017年7月14日 下午2:58:54
     * @param tableName
     * @param unId
     */
    void deleteByUnId(String tableName, String unId);

    /**
     * 查询条数
     * 
     * @author lianhanwen
     * @date 2017年7月21日 下午2:30:13
     * @param tableName
     * @param unId
     * @param result
     * @return
     */
    int getCountByUnIdAndResult(String tableName, String unId, Integer result);

    /**
     * 根据文件的Unid修改状态
     * 
     * @author lianhanwen
     * @date 2017年7月27日 上午11:24:23
     * @param baseDataEntity
     */
    void updateDataByUnId(BaseDataEntity baseDataEntity);

}
