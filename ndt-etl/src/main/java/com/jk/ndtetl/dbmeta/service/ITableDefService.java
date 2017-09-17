package com.jk.ndtetl.dbmeta.service;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.TableDef;

/**
 * @ClassName: SheetInfoService
 * @Description: 上传时可能需要的操作方法
 * @author lianhanwen
 * @date 2017年5月18日 上午11:24:54
 *
 */

public interface ITableDefService extends IBaseService<TableDef> {

    /**
     * 根据category和businessTypeId查询唯一的table, false:表示数据库已存在这样的组合,true:不存在
     * 
     * @author lianhanwen
     * @date 2017年7月8日 下午3:39:12
     * @param category
     * @param businessTypeId
     * @return
     */
    TableDef getTableByCategoryAndBusinessType(String category, Integer businessTypeId);

    /**
     * 查询某一个业务类型下的一个组织机构的数据
     * 
     * @author lianhanwen
     * @date 2017年7月8日 下午5:55:57
     * @param category
     * @param OrgId
     * @param businessTypeId
     * @return
     */
    List<Map<String, Object>> getDataByOrgAndBusinessType(String category, Integer orgId, Integer businessTypeId,
            BasePage page);

    /**
     * 根据确定的表和orgId确定唯一的dataFileType
     * 
     * @author lianhanwen
     * @date 2017年7月10日 下午3:03:11
     * @param category
     * @param orgId
     * @param tableDef
     * @return
     */
    DataFileType getDataFileTypeByTableAndOrg(String category, Integer etlFileTypeId, Integer orgId, Integer tableId);

    /**
     * 根据表id查询规则
     * 
     * @author lianhanwen
     * @date 2017年7月17日 下午4:58:53
     * @param tableId
     * @return
     */
    TableDef getRuleByTableId(Integer tableId);

}
