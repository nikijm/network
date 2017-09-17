package com.jk.ndtetl.etl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.CommonQueryObj;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.etl.BaseDataEntity;
import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.etl.service.ICommonService;
import com.jk.ndtetl.util.Checker;

/**
 * @ClassName: CommonServiceImpl
 * @Description: 公共操作类实现
 * @author lianhanwen
 * @date 2017年6月22日 下午5:41:03
 *
 */
@Service("commonService")
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private CommonDao commonDao;

    /**
     * 
     * @Description: 查询制定表所有的数据
     * @author lianhanwen
     * @date 2017年5月18日 下午2:44:57
     * @param tableName
     * @param unquieId
     * @return
     */
    @Override
    public List<Map<String, Object>> listAll(String tableName, String unquieId) {
        return commonDao.listAll(tableName, unquieId);
    }

    /**
     * @Description: 根据分页条件查询数据
     * @author lianhanwen
     * @date 2017年6月13日 下午6:43:13
     * @param tableName
     * @param unquieId
     * @param page
     * @return
     */
    @Override
    public List<Map<String, Object>> listByPage(TableDef tableDef, BasePage page) {
        if (Checker.isNullOrEmpty(tableDef)) {
            return null;
        }
        String tableName = tableDef.getTableName();
        List<ColumnDef> columns = tableDef.getColumns();
        List<String> isSearchColumns = new ArrayList<>();
        for (ColumnDef column : columns) {
            Character isSearch = column.getIsSearch();
            if (isSearch != null && isSearch == 'Y') {
                isSearchColumns.add(column.getColumnName());
            }
        }
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        return commonDao.listByPage(tableName, isSearchColumns, page);

    }

    /**
     * @Description: 根据多个id查询数据列表
     * @author lianhanwen
     * @date 2017年6月13日 下午6:42:42
     * @param tableName
     * @param unquieId
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, Object>> listByIds(String tableName, String unquieId, List<Integer> ids) {

        return commonDao.listByIds(tableName, unquieId, ids);
    }

    /**
     * 
     * @Description: 将dataTable中的数据保存到数据库表中
     * @author lianhanwen
     * @date 2017年5月18日 下午2:45:28
     * @param dataTable
     * @return
     */
    @Override
    public void saveTableContent(DataTable dataTable) {
        commonDao.saveTableContent(dataTable);
    }

    /**
     * @Description: 修改数据
     * @author lianhanwen
     * @date 2017年5月18日 下午2:45:28
     * @param dataTable
     * @return
     */
    @Override
    public void updateDataById(BaseDataEntity baseDataEntity) {
        commonDao.updateDataById(baseDataEntity);
    }

    @Override
    public void updateDataByUnId(BaseDataEntity baseDataEntity) {
        commonDao.updateDataByUnId(baseDataEntity);
    }

    @Override
    public void updateDataByIds(BaseDataEntity baseDataEntity, List<Integer> ids) {
        commonDao.updateDataByIds(baseDataEntity, ids);
    }

    /**
     * 
     * @Description: 根据查询对象查询数据
     * @author fangwei
     * @date 2017年6月5日 下午2:29:44
     * @param tableName
     * @param queryList
     * @return
     */
    @Override
    public List<Map<String, Object>> listByQueryObj(String tableName, List<CommonQueryObj> queryList) {
        return commonDao.listByQueryObj(tableName, queryList);
    }

    /**
     * @Description: 判断列名是否重复
     * @author lianhanwen
     * @date 2017年6月24日 下午4:57:42
     * @param tableName
     * @param NewColumnName
     * @return false 重复
     */
    @Override
    public Boolean isNewTableNameAvailable(String newTableName) {
        Integer num = commonDao.isNewTableNameAvailable(newTableName);
        if (num > 0) {
            return false;
        }
        return true;
    }

    /**
     * @Description: 判断表名是否重复
     * @author lianhanwen
     * @date 2017年6月24日 下午4:57:59
     * @param newTableName
     * @return false 重复
     */
    @Override
    public Boolean isNewColumnNameAvailable(String tableName, String NewColumnName) {
        Integer num = commonDao.isNewColumnNameAvailable(tableName, NewColumnName);
        if (num > 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getColumnNames(String tableName) {
        List<String> columnNames = commonDao.getColumnNames(tableName);
        return columnNames;
    }

    @Override
    public void deleteByUnId(String tableName, String unId) {
        commonDao.deleteByUnId(tableName, unId);
    }

    @Override
    public void saveCacheTableContent(DataTable dataTable) {
        commonDao.saveCacheTableContent(dataTable);
    }

    @Override
    public int getCountByUnIdAndResult(String tableName, String unId, Integer result) {
        return commonDao.getCountByUnIdAndResult(tableName, unId, result);
    }

}
