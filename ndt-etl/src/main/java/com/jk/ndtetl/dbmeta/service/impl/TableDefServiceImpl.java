package com.jk.ndtetl.dbmeta.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.dao.DataFileDao;
import com.jk.ndtetl.dbmeta.dao.DataFileTypeDao;
import com.jk.ndtetl.dbmeta.dao.TableDefDao;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.util.Checker;

/**
 * @ClassName: TableDefServiceImpl
 * @Description: TableDef实现类
 * @author lianhanwen
 * @date 2017年6月22日 下午5:00:46
 *
 */
@Service("tableDefService")
public class TableDefServiceImpl extends BaseServiceImpl<TableDef> implements ITableDefService {

    @Autowired
    private TableDefDao tableDefDao;

    @Autowired
    private DataFileTypeDao dataFileTypeDao;

    @Autowired
    private DataFileDao dataFileDao;

    @Autowired
    private CommonDao commonDao;

    @Override
    protected BaseDao<TableDef> getDao() {
        return tableDefDao;
    }

    @Override
    public TableDef getTableByCategoryAndBusinessType(String category, Integer businessTypeId) {
        BasePage page = new BasePage();
        Map<String, Object> param = new HashMap<String, Object>();
        if (Checker.isNotNullOrEmpty(category) && Checker.isNotNullOrEmpty(businessTypeId)) {
            param.put("category", category);
            param.put("businessTypeId", businessTypeId);
            page.setParam(param);
            List<TableDef> listByPage = tableDefDao.listByPage(page);
            if (null != listByPage && listByPage.size() > 0) {
                return listByPage.get(0);
            }
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> getDataByOrgAndBusinessType(String category, Integer orgId, Integer businessTypeId,
            BasePage page) {

        // 根据category和businessTypeId确定唯一表
        TableDef tableDef = getTableByCategoryAndBusinessType(category, businessTypeId);
        if (tableDef == null) {
            return null;
        }
        String tableName = tableDef.getTableName();

        // 获得需要模糊查询的列
        List<ColumnDef> columns = tableDef.getColumns();
        List<String> isSearchColumns = new ArrayList<>();
        for (ColumnDef column : columns) {
            Character isSearch = column.getIsSearch();
            if (isSearch != null && isSearch == 'Y') {
                isSearchColumns.add(column.getColumnName());
            }
        }
        // 获取当前业务类型下所有的数据
        if (Checker.isNullOrEmpty(orgId)) {
            PageHelper.startPage(page.getCurrent(), page.getPage_size());
            List<Map<String, Object>> allDatas = commonDao.listByPage(tableName, isSearchColumns, page);
            return allDatas;
        }

        List<String> unIds = getUnIdsByOrgId(category, orgId, tableDef);
        if (unIds == null || unIds.size() <= 0) {
            return new ArrayList<Map<String, Object>>();
        }
        // 根据unIds查询数据
        Map<String, Object> param = page.getParam();
        param.put("unIds", unIds);
        page.setParam(param);
        // 分页
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        List<Map<String, Object>> orgDatas = commonDao.listByPage(tableName, isSearchColumns, page);
        return orgDatas;
    }

    private List<String> getUnIdsByOrgId(String category, Integer orgId, TableDef tableDef) {
        // 根据确定的表和orgId确定唯一的dataFileType
        DataFileType dataFileType = getDataFileTypeByTableAndOrg(category, null, orgId, tableDef.getEtlTableId());
        if (dataFileType==null) {
            return null;
        }
        // 根据dataFileType查询所有的文件
        BasePage page = new BasePage();
        Map<String, Object> param = new HashMap<>();
        param.put("etlFileTypeId", dataFileType.getEtlFileTypeId());
        page.setParam(param);
        List<DataFile> listByPage2 = dataFileDao.listByPage(page);
        if (null == listByPage2 || listByPage2.size() <= 0) {
            return null;
        }
        List<String> unIds = new ArrayList<>();
        for (DataFile dataFile : listByPage2) {
            unIds.add(dataFile.getUnId());
        }
        return unIds;
    }

    @Override
    public DataFileType getDataFileTypeByTableAndOrg(String category, Integer etlFileTypeId, Integer orgId,
            Integer tableId) {
        BasePage page = new BasePage();
        Map<String, Object> param = new HashMap<>();
        if (category == TableDef.CATEGORY_CACHE) {
            param.put("tableIdCache", tableId);
        }
        if (category == TableDef.CATEGORY_CLEAN) {
            param.put("tableIdClean", tableId);
        }
        if (category == TableDef.CATEGORY_CONVERT) {
            param.put("tableIdConvert", tableId);
        }
        if (category == TableDef.CATEGORY_VERIFY) {
            param.put("tableIdValidate", tableId);
        }
        param.put("orgId", orgId);
        param.put("etlFileTypeId", etlFileTypeId);
        page.setParam(param);
        List<DataFileType> listByPage = dataFileTypeDao.listByPage(page);
        if (null == listByPage || listByPage.size() <= 0) {
            return null;
        }
        DataFileType dataFileType = listByPage.get(0);
        return dataFileType;
    }

    @Override
    public TableDef getRuleByTableId(Integer tableId) {
        TableDef tableDef = tableDefDao.getRuleByTableId(tableId);
        return tableDef;
    }
}
