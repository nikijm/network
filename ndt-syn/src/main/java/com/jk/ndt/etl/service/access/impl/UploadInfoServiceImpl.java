package com.jk.ndt.etl.service.access.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.dao.access.SheetInfoDao;
import com.jk.ndt.etl.dao.access.UploadInfoDao;
import com.jk.ndt.etl.dao.common.CommonDao;
import com.jk.ndt.etl.dao.logs.SheetLogDao;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.access.UploadInfo;
import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.service.BaseServiceImpl;
import com.jk.ndt.etl.service.access.UploadInfoService;
import com.jk.ndt.etl.utility.Checker;

@Service("uploadInfoService")
public class UploadInfoServiceImpl extends BaseServiceImpl<UploadInfo> implements UploadInfoService {
    @Autowired
    private UploadInfoDao uploadInfoDao;
    @Autowired
    private SheetInfoDao sheetInfoDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SheetLogDao sheetLogDao;

    @Override
    protected BaseDao<UploadInfo> getDao() {
        return uploadInfoDao;
    }

    @Override
    public Boolean isRepeat(String sha1) {
        UploadInfo uploadInfo = uploadInfoDao.getBySha1(sha1);
        if (uploadInfo != null) {
            return true;
        }
        return false;
    }

    @Override
    public void cache(Integer id, Admin user, List<DataTable> list) throws Exception {
        try {
        UploadInfo uploadInfo = uploadInfoDao.getById(id);
        // 保存sheet信息
            for (int i = 0; i < list.size(); i++) {
                SheetInfo sheetInfo = new SheetInfo();
                sheetInfo.setStatus(Constant.SHEET_STATUS_UNVALIDATED);
                sheetInfo.setUpload(uploadInfo);
                // 创建表,并保存内容
                DataTable dataTable = list.get(i);

                List<String> header = dataTable.getHeader();

                // TODO 设计要求暂时写死,待修改
                // if (header.size() != columns.length) {
                // model.addAttribute("msg", "表头数与列名数对应不上");
                // return "/error";
                // }
                // header = Arrays.asList(columns);
                Map<String, String> columnsMap = new LinkedHashMap<String, String>();
                for (int j = 0; j < header.size(); j++) {
                    columnsMap.put("A" + j, header.get(j) == null ? "" : header.get(j));
                    header.set(j, "A" + j);

                }
                String columnsJson = JSONObject.toJSONString(columnsMap);
                sheetInfo.setColumns(columnsJson);
                dataTable.setHeader(header);
                sheetInfoDao.save(sheetInfo);
                // 创建original表
                dataTable.setTableName(Constant.ORIGINAL_TABLE_PREFIX + sheetInfo.getId());
                try {
                    createTable(dataTable);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    throw new Exception("创建表"+dataTable.getTableName()+"失败");
                }
                
                try {
                    commonDao.saveTableContent(dataTable);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("缓存数据失败,请检查文件内容格式是否规范!!");
                }

                // 创建cleaned表
                dataTable.setTableName(Constant.CLEANED_TABLE_PREFIX + sheetInfo.getId());
                try {
                    createTable(dataTable);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new Exception("创建表"+dataTable.getTableName()+"失败");
                }
                // 记录日志
                SheetLog sheetLog = new SheetLog(sheetInfo, user, Constant.SHEET_LOG_CACHED, new Date(), null, null,
                        Constant.ORIGINAL_TABLE_PREFIX + sheetInfo.getId());
                try {
                    sheetLogDao.save(sheetLog);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("记录日志失败");
                }

            }
            uploadInfo.setStatus(Constant.FILE_STATUS_CACHED);
            uploadInfo.setSheetsNum(list.size());
            uploadInfoDao.update(uploadInfo);
        } catch (Exception e) {
            e.printStackTrace();
            // 删除表和序列
            List<SheetInfo> byUploadId2 = sheetInfoDao.getByUploadId(id);
            if (Checker.isNotNullOrEmpty(byUploadId2)) {
                for (SheetInfo sheetInfo2 : byUploadId2) {
                    Integer sheetId = sheetInfo2.getId();
                    String tableName = Constant.ORIGINAL_TABLE_PREFIX + sheetId;
                    deleteTable(tableName);
                    tableName = Constant.CLEANED_TABLE_PREFIX + sheetId;
                    deleteTable(tableName);
                    sheetLogDao.deleteCacheLogBySheetId(sheetId);
                }
            }
            sheetInfoDao.deleteByUploadId(id);
            throw e;
        }
    }

    private void deleteTable(String tableName) {
        commonDao.deleteTable(tableName);
        commonDao.deleteSeq(tableName);
    }

    private void createTable(DataTable dataTable) {
        commonDao.createTable(dataTable);
        commonDao.createSeq(dataTable.getTableName());
    }

}
