package com.jk.ndtetl.dbmeta.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileLog;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.dao.DataFileDao;
import com.jk.ndtetl.dbmeta.dao.DataFileLogDao;
import com.jk.ndtetl.dbmeta.dao.DataFileTypeDao;
import com.jk.ndtetl.dbmeta.dao.TableDefDao;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.etl.service.FileConverter;
import com.jk.ndtetl.etl.service.impl.CSVConverter;
import com.jk.ndtetl.etl.service.impl.ExcelConverter;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.schedule.CacheAutoExecutor;
import com.jk.ndtetl.schedule.conf.TurnOnOff;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.QueryParam;

/**
 * @ClassName: DataFileServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lianhanwen
 * @date 2017年6月24日 上午9:11:25
 *
 */
@Service("dataFileService")
public class DataFileServiceImpl extends BaseServiceImpl<DataFile> implements IDataFileService {

    @Autowired
    private DataFileDao dataFileDao;

    @Autowired
    private DataFileLogDao dataFileLogDao;

    @Autowired
    private DataFileTypeDao dataFileTypeDao;

    @Autowired
    private TableDefDao tableDefDao;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private CSVConverter cSVConverter;

    @Autowired
    private ExcelConverter excelConverter;

    @Resource(name = "cacheAutoExecutor")
    private CacheAutoExecutor cacheAutoExecutor;

    @Override
    protected BaseDao<DataFile> getDao() {
        return dataFileDao;
    }

    @Override
    public Boolean isRepeat(String sha1) {
        List<DataFile> bySha1 = dataFileDao.getBySha1(sha1);
        if (bySha1 != null && bySha1.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveDatafiles(List<DataFile> dataFiles) {
        for (DataFile dataFile : dataFiles) {
            dataFileDao.save(dataFile);
        }
    }

    @Override
    public int getFileNumFromTodayByUserId(Integer UserId) {
        return dataFileDao.getFileNumFromTodayByUserId(UserId);
    }

    /**
     * @Description: 解析并缓存
     * @author lianhanwen
     * @date 2017年5月19日 上午10:52:13
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> analyzeAndCache(DataFile dataFile, User user, String realPath) {
        Map<String, Object> map = new HashMap<String, Object>();

        map = cache(realPath, dataFile, map);
        // 日志处理
        journalling(dataFile, user, map);
        return map;
    }

    private void journalling(DataFile dataFile, User user, Map<String, Object> map) {
        String errorMsg = (String) map.get("error");
        Integer totalNum = (Integer) map.get("lastRowNum") + 1;
        if (Checker.isNotNullOrEmpty(errorMsg)) {
            dataFile.setStatusCache(DataFile.DATA_STATUS_FAILED);
            dataFileDao.update(dataFile);
            writeCacheLog(user, totalNum, 0, dataFile, errorMsg);
        }
        String contenMsg = (String) map.get("content");
        if (Checker.isNotNullOrEmpty(contenMsg)) {
            int contentNum = excelConverter.getReadPosSize();
            writeCacheLog(user, totalNum, contentNum, dataFile, contenMsg);
        }
        if (dataFile.getStatusCache() == DataFile.DATA_STATUS_FINISHED) {
            int unKnowNum = dataFileLogDao.getUnKnowNumByFileId(dataFile.getEtlDatafileId());
            writeCacheLog(user, totalNum, unKnowNum, dataFile, "缓存成功");
        }
    }

    /**
     * 记录日志
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午9:26:39
     * @param user
     * @param map
     * @param dataFile
     */
    private void writeCacheLog(User user, Integer totalNum, Integer contentNum, DataFile dataFile, String errorMsg) {
        DataFileLog dataFileLog = new DataFileLog();
        dataFileLog.setAction(DataFile.ACTION_CACHE);
        dataFileLog.setCreated(new Date());
        if (user != null) {
            dataFileLog.setCreatedBy(user.getId());
        }
        dataFileLog.setEtlDatafileId(dataFile.getEtlDatafileId());
        dataFileLog.setRunning(dataFile.getStatusCache());
        Integer successNum = dataFile.getSheetStartRow();
        dataFileLog.setSuccessNum(successNum - contentNum);
        dataFileLog.setTotalNum(totalNum);
        dataFileLog.setMessage(errorMsg);
        dataFileLog.setUnknownNum(contentNum);
        dataFileLogDao.save(dataFileLog);
    }

    /**
     * 缓存
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午9:26:58
     * @param id
     * @param realPath
     * @param dataFile
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> cache(String realPath, DataFile dataFile, Map<String, Object> map) {

        Integer sheetIndex = dataFile.getSheetIndex();
        Integer sheetStartRow = dataFile.getSheetStartRow();
        String path = realPath + dataFile.getPath();
        String fileName = dataFile.getFileName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        // 解析文件
        List<DataTable> list = new ArrayList<>();
        try {
            list = handleFile(path, ext, sheetIndex, sheetStartRow);
        }
        catch (Exception e) {
            e.printStackTrace();
            map.put("error", e.getMessage());
            return map;
        }
        DataTable dataTable = list.get(0);

        // 记录日志用
        map.put("lastRowNum", dataTable.getLastRowNum());
        // 如果content为空就不执行插入
        if (Checker.isNullOrEmpty(dataTable.getContent())) {
            dataFile.setStatusCache(DataFile.DATA_STATUS_STARTED);
            sheetStartRow = sheetStartRow + excelConverter.getReadPosSize();
            dataFile.setSheetStartRow(sheetStartRow);
            if (sheetStartRow >= dataTable.getLastRowNum()) {
                dataFile.setSheetStartRow(dataTable.getLastRowNum());
                dataFile.setStatusCache(DataFile.DATA_STATUS_FINISHED);
                // 判断是否是自动调度,是就把清洗状态改为ready
                if (TurnOnOff.getInstance().isAuto()) {
                    dataFile.setStatusClean(DataFile.DATA_STATUS_READY);
                }
            }
            dataFileDao.update(dataFile);
            map.put("content", "空白内容");
            return map;
        }
        // 字段对齐
        DataFileType dataFileType = dataFile.getDataFileType();
        List<ColumnDef> columns = new ArrayList<ColumnDef>();
        String fieldsAlign = null;
        if (Checker.isNotNullOrEmpty(dataFileType)) {
            fieldsAlign = dataFileType.getFieldsAlign();
            DataFileType fileType = dataFileTypeDao.getById(dataFileType.getEtlFileTypeId());
            TableDef tableCache = fileType.getTableCache();
            if (Checker.isNotNullOrEmpty(tableCache.getEtlTableId())) {
                dataTable.setTableName(tableCache.getTableName().toUpperCase());
                TableDef tableDef = tableDefDao.getById(tableCache.getEtlTableId());
                if (Checker.isNotNullOrEmpty(tableDef)) {
                    columns = tableDef.getColumns();
                }
            }
        }
        List<String> header = new ArrayList<>();
        List<String> header1 = dataTable.getHeader();
        String header2 = dataFile.getHeader();
        List<Integer> unUseColumnNums = new ArrayList<>();
        Map<String, String> fieldsAlignMap = JSON.parseObject(fieldsAlign, Map.class);
        if (Checker.isNullOrEmpty(header2)) {
            // if (header.size() != columns.size()) {
            // map.put("error", "excel中表头数与列名数对应不上,请检查");
            // return map;
            // }
            if (Checker.isNotNullOrEmpty(fieldsAlignMap) && Checker.isNotNullOrEmpty(columns)) {
                // 使用表头来对应列名
                // for (String column : header) {
                // header.set(header.indexOf(column),
                // fieldsAlignMap.get(column));
                // }
                // 使用列号来对应列名
                for (int i = 0; i < header1.size(); i++) {
                    String columnName = fieldsAlignMap.get(i + "");
                    if (columnName != null) {
                        header.add(columnName);
                    }
                    else {
                        unUseColumnNums.add(i + 1);
                    }
                }

            }
        }
        else {
            header = JSON.parseObject(header2, List.class);
            for (int i = 0; i < dataTable.getContent().get(0).size(); i++) {
                if (fieldsAlignMap.get(i + "") == null) {
                    unUseColumnNums.add(i + 1);
                }
            }
        }
        // 保存数据
        dataTable.setHeader(header);
        dataTable.setUnUseColumnNums(unUseColumnNums);
        dataTable.setDatafile_unid(dataFile.getUnId());
        commonDao.saveCacheTableContent(dataTable);

        dataFile.setHeader(JSON.toJSONString(header));
        dataFile.setStatusCache(DataFile.DATA_STATUS_STARTED);
        sheetStartRow = sheetStartRow + excelConverter.getReadPosSize();
        dataFile.setSheetStartRow(sheetStartRow);
        if (sheetStartRow >= dataTable.getLastRowNum()) {
            dataFile.setSheetStartRow(dataTable.getLastRowNum());
            dataFile.setStatusCache(DataFile.DATA_STATUS_FINISHED);
            // 判断是否是自动调度,是就把清洗状态改为ready
            if (TurnOnOff.getInstance().isAuto()) {
                dataFile.setStatusClean(DataFile.DATA_STATUS_READY);
            }
        }
        dataFileDao.update(dataFile);

        return map;
    }

    /**
     * 解析
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午9:27:53
     * @param filePath
     * @param extend
     * @param sheetIndex
     * @param sheetStartRow
     * @return
     * @throws Exception
     */
    private List<DataTable> handleFile(String filePath, String extend, Integer sheetIndex, Integer sheetStartRow)
            throws Exception {
        if (Checker.isNotNullOrEmpty(sheetIndex)) {
            excelConverter.setSelectedSheetIdx(sheetIndex);
        }
        if (Checker.isNotNullOrEmpty(sheetStartRow)) {
            excelConverter.setStartReadPos(sheetStartRow);
        }
        FileConverter converter = null;
        switch (extend) {
        case "xls":
            converter = excelConverter;
            break;
        case "xlsx":
            converter = excelConverter;
            break;
        case "csv":
            converter = cSVConverter;
            break;
        default:
            converter = excelConverter;
            break;
        }
        List<DataTable> list = converter.convert(filePath);
        if (Checker.isNullOrEmpty(list)) {
            throw new IOException("解析出错或文件内容为空");
        }
        return list;
    }

    @Override
    public List<DataFile> listByCacheStatus(QueryParam queryParam) {
        List<DataFile> dataFiles = dataFileDao.listByCacheStatus(queryParam);
        return dataFiles;
    }

    @Override
    public List<DataFile> listFileToProcess(QueryParam queryParam) {
        List<DataFile> dataFiles = dataFileDao.listFileToProcess(queryParam);
        return dataFiles;
    }

    @Override
    public boolean rollBack(Integer fileId) throws CustomException {
        DataFile dataFile = dataFileDao.getById(fileId);
        if (Checker.isNullOrEmpty(dataFile)) {
            throw new CustomException("this file is null.");
        }

        String unId = dataFile.getUnId();
        DataFileType dataFileType = dataFile.getDataFileType();
        if (Checker.isNullOrEmpty(dataFileType)) {
            throw new CustomException("this file has not dataFileType.");
        }

        DataFileType fileType = dataFileTypeDao.getById(dataFileType.getEtlFileTypeId());
        TableDef tableCache = fileType.getTableCache();
        if (Checker.isNullOrEmpty(tableCache.getEtlTableId())) {
            throw new CustomException("this file has not cacheTable.");
        }

        String tableName = tableCache.getTableName().toUpperCase();
        if (tableName == null || unId == null) {
            throw new CustomException("cannot determined datas to be deleted ");
        }
        commonDao.deleteByUnId(tableName, unId);
        dataFile.setStatusCache(DataFile.DATA_STATUS_NOTSTART);
        dataFileDao.update(dataFile);
        cacheAutoExecutor.removeMap(String.valueOf(fileId));
        return true;
    }
}
