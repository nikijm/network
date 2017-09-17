package com.jk.ndtetl.dbmeta.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.DbMeta;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.dao.ColumnDefDao;
import com.jk.ndtetl.dbmeta.dao.DataFileDao;
import com.jk.ndtetl.dbmeta.dao.TableDefDao;
import com.jk.ndtetl.dbmeta.service.IDbMetaService;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.etl.dao.DDLExecutorDao;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.etl.TransformerUtil;

/**
 * @ClassName: DbMetaServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lianhanwen
 * @date 2017年6月22日 下午3:09:49
 *
 */
@Service("dbMetaService")
public class DbMetaServiceImpl implements IDbMetaService {

    @Autowired
    private TableDefDao tableDefDao;

    @Autowired
    private DataFileDao dataFileDao;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private ColumnDefDao columnDefDao;

    @Autowired
    private DDLExecutorDao ddlExecutorDao;

    @Override
    public DbMeta queryDbMetaByETLFileTypeId(Integer fileTypeId, String etlOp) {

        return null;
    }

    @Override
    public TableDef queryDbMetaByTableName(String tableName) {
        TableDef tableDef = tableDefDao.getByTableName(tableName);
        return tableDef;
    }

    @Override
    public void saveTableDefAndColumns(TableDef tableDef) {
        tableDef.setTableName(tableDef.getTableName().toUpperCase());
        List<ColumnDef> columns = tableDef.getColumns();
        tableDefDao.save(tableDef);
        for (ColumnDef columnDef : columns) {
            columnDef.setColumnName(columnDef.getColumnName().toUpperCase());
            columnDef.setTableDef(tableDef);
            columnDefDao.save(columnDef);
        }
    }

    @Override
    public Map<String, String> createTable(TableDef tableDef) {
        Map<String, String> map = new HashMap<>();
        try {
            ddlExecutorDao.createTable(tableDef);
            ddlExecutorDao.createSeq(tableDef.getTableName());
            saveTableDefAndColumns(tableDef);
            // throw new CustomException("错错错");
        }
        catch (Exception e) {
            e.printStackTrace();
            ddlExecutorDao.deleteTable(tableDef.getTableName());
            ddlExecutorDao.deleteSeq(tableDef.getTableName());
            columnDefDao.deleteByTableId(tableDef.getEtlTableId());
            tableDefDao.deleteById(tableDef.getEtlTableId());
            map.put("error", "创建表失败");
        }
        return map;
    }

    @Override
    public void updateTableAndColumn(TableDef tableDef) {
        tableDef.setTableName(tableDef.getTableName().toUpperCase());
        List<ColumnDef> columns = tableDef.getColumns();
        tableDefDao.update(tableDef);
        for (ColumnDef columnDef : columns) {
            columnDef.setColumnName(columnDef.getColumnName().toUpperCase());
            columnDefDao.update(columnDef);
        }
    }

    @Override
    public File[] readDirectory(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        // System.out.println("该目录下对象个数：" + tempList.length);
        // for (int i = 0; i < tempList.length; i++) {
        // if (tempList[i].isFile()) {
        // System.out.println("文 件：" + tempList[i]);
        // }
        // if (tempList[i].isDirectory()) {
        // System.out.println("文件夹：" + tempList[i]);
        // }
        // }
        return tempList;
    }

    @Override
    public List<Map<String, Object>> getContrastDatas(Integer dataFileId, String category, BasePage page)
            throws CustomException {

        DataFile dataFile = dataFileDao.getTableById(dataFileId);
        if (Checker.isNullOrEmpty(dataFile)) {
            throw new CustomException("当前文件不存在,请联系管理员处理");
        }

        String unId = dataFile.getUnId();
        DataFileType dataFileType = dataFile.getDataFileType();
        String cacheTableName = dataFileType.getTableCache().getTableName();
        String cleanTableName = dataFileType.getTableClean().getTableName();
        String convertTableName = dataFileType.getTableConvert().getTableName();
        String validateTableName = dataFileType.getTableValidate().getTableName();

        if (TableDef.CATEGORY_CLEAN.equals(category)) {
            return getDatas(page, unId, cacheTableName, cleanTableName);
        }
        if (TableDef.CATEGORY_CONVERT.equals(category)) {
            return getDatas(page, unId, cleanTableName, convertTableName);
        }
        if (TableDef.CATEGORY_VERIFY.equals(category)) {
            return getDatas(page, unId, convertTableName, validateTableName);
        }
        return null;
    }

    private List<Map<String, Object>> getDatas(BasePage page, String unId, String sourceTableName,
            String targetTableName) throws CustomException {

        Integer num1 = commonDao.isNewTableNameAvailable(sourceTableName);
        Integer num2 = commonDao.isNewTableNameAvailable(targetTableName);
        if (num1 <= 0 || num2 <= 0) {
            throw new CustomException("表或视图不存在,请联系管理员处理");
        }
        List<String> sourceColumnNames = commonDao.getColumnNames(sourceTableName);
        List<String> targetColumnNames = commonDao.getColumnNames(targetTableName);
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        return commonDao.getContrastDatas(sourceTableName, targetTableName, unId, sourceColumnNames, targetColumnNames);
    }

    @Override
    public Map<String, Object> getContrastHeader(Integer dataFileId, String category) throws CustomException {
        DataFile dataFile = dataFileDao.getTableById(dataFileId);
        if (Checker.isNullOrEmpty(dataFile)) {
            throw new CustomException("当前文件不存在,请联系管理员处理");
        }

        DataFileType dataFileType = dataFile.getDataFileType();
        Integer cacheTableId = dataFileType.getTableCache().getEtlTableId();
        Integer cleanTableId = dataFileType.getTableClean().getEtlTableId();
        Integer convertTableId = dataFileType.getTableConvert().getEtlTableId();
        Integer validateTableId = dataFileType.getTableValidate().getEtlTableId();

        if (TableDef.CATEGORY_CLEAN.equals(category)) {
            return transColumns(cacheTableId, cleanTableId);
        }
        if (TableDef.CATEGORY_CONVERT.equals(category)) {
            return transColumns(cleanTableId, convertTableId);
        }
        if (TableDef.CATEGORY_VERIFY.equals(category)) {
            return transColumns(convertTableId, validateTableId);
        }
        return null;
    }

    private Map<String, Object> transColumns(Integer sourceTableId, Integer targetTableId) {
        List<ColumnDef> sourceColumns = columnDefDao.getByTableId(sourceTableId);
        List<ColumnDef> targetColumns = columnDefDao.getByTableId(targetTableId);
        return TransformerUtil.getContrastHeader(sourceColumns, targetColumns);
    }

    @Override
    public List<Map<String, Object>> getCacheDatas(Integer dataFileId, BasePage page) throws CustomException {
        DataFile dataFile = dataFileDao.getTableById(dataFileId);
        if (Checker.isNullOrEmpty(dataFile)) {
            throw new CustomException("当前文件不存在,请联系管理员处理");
        }

        String unId = dataFile.getUnId();
        DataFileType dataFileType = dataFile.getDataFileType();
        String cacheTableName = dataFileType.getTableCache().getTableName();
        Integer cacheTableId = dataFileType.getTableCache().getEtlTableId();
        List<ColumnDef> columns = columnDefDao.getByTableId(cacheTableId);
        Map<String, Object> header = TransformerUtil.getHeader(columns);

        Map<String, Object> param = new HashMap<>();
        param.put("unId", unId);
        page.setParam(param);
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        List<Map<String, Object>> listByPage = commonDao.listByPage(cacheTableName, null, page);
        listByPage.add(0, header);
        return listByPage;
    }

}
