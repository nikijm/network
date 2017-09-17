package com.jk.ndt.etl.service.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.entity.CommonQueryObj;
import com.jk.ndt.etl.entity.Page;

/**
 * 
 * @ClassName: CommonService
 * @Description: 公共操作服务类
 * @author fangwei
 * @date 2017年5月16日 下午3:56:53
 *
 */
public interface CommonService {

    /**
     * @Description: 根据sql创建表
     * @author lianhanwen
     * @date 2017年5月18日 下午2:44:38
     * @param dataTable
     * @return
     */
    void createTable(DataTable dataTable);
    void createSeq(String tableName);

    /**
     * 
     * @Description: 查询制定表所有的数据
     * @author lianhanwen
     * @date 2017年5月18日 下午2:44:57
     * @param tableName
     * @param unquieId
     * @return
     */
    List<Map<String, Object>> listAll(@Param("tableName") String tableName, @Param("unquieId") String unquieId);

    /**
     * 
     * @Description: 将dataTable中的数据保存到数据库表中
     * @author lianhanwen
     * @date 2017年5月18日 下午2:45:28
     * @param dataTable
     * @return
     */
    void saveTableContent(DataTable dataTable);

    /**
     * 
     * @Description: 根据表名删除表
     * @author lianhanwen
     * @date 2017年5月22日 下午5:26:08
     * @param tableName
     */
    void deleteTable(@Param("tableName") String tableName);

    /**
     * 
     * @Description: 根据表名删除相应表的序列
     * @author lianhanwen
     * @date 2017年5月22日 下午5:26:34
     * @param tableName
     */
    void deleteSeq(@Param("tableName") String tableName);

    List<Map<String, Object>> listByPage(String tableName, String unquieId, Page page);

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

    List<Map<String, Object>> listByIds(String tableName, String unquieId, List<Integer> ids);

}
