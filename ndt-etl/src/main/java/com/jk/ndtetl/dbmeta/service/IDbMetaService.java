package com.jk.ndtetl.dbmeta.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.DbMeta;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.exception.CustomException;

/**
 * @ClassName: IDbMetaService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lianhanwen
 * @date 2017年6月22日 下午3:09:54
 *
 */
public interface IDbMetaService {

    /**
     * 根据文件类型id查询数据
     * 
     * @author lianhanwen
     * @date 2017年7月19日 下午3:42:58
     * @param fileTypeId
     * @param etlOp
     * @return
     */
    DbMeta queryDbMetaByETLFileTypeId(Integer fileTypeId, String etlOp);

    /**
     * 根据表名称查询数据
     * 
     * @author lianhanwen
     * @date 2017年7月19日 下午3:40:22
     * @param tableName
     * @return
     */
    TableDef queryDbMetaByTableName(String tableName);

    /**
     * 保存表和列
     * 
     * @author lianhanwen
     * @date 2017年7月19日 下午3:40:09
     * @param tableDef
     */
    void saveTableDefAndColumns(TableDef tableDef);

    /**
     * 更新表和列
     * 
     * @author lianhanwen
     * @date 2017年7月19日 下午3:39:53
     * @param tableDef
     */
    void updateTableAndColumn(TableDef tableDef);

    /**
     * 创建表
     * 
     * @author lianhanwen
     * @date 2017年7月19日 下午3:39:43
     * @param tableDef
     * @return
     */
    Map<String, String> createTable(TableDef tableDef);

    /**
     * 读取文件夹的数据
     * 
     * @author lianhanwen
     * @date 2017年7月19日 下午3:39:23
     * @param path
     * @return
     */
    File[] readDirectory(String path);

    /**
     * 查询对比数据
     * 
     * @author lianhanwen
     * @date 2017年7月24日 下午5:30:11
     * @param dataFileId
     * @param category
     * @param page
     * @return
     * @throws CustomException 
     */

    List<Map<String, Object>> getContrastDatas(Integer dataFileId, String category, BasePage page) throws CustomException;

    Map<String, Object> getContrastHeader(Integer dataFileId, String category) throws CustomException;

    List<Map<String, Object>> getCacheDatas(Integer dataFileId, BasePage page) throws CustomException;

}
