package com.jk.ndt.etl.service.rule.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.dao.access.SheetInfoDao;
import com.jk.ndt.etl.dao.common.CommonDao;
import com.jk.ndt.etl.dao.logs.SheetLogDao;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.entity.rule.CleanResult;
import com.jk.ndt.etl.entity.rule.CleanerColumnPO;
import com.jk.ndt.etl.entity.rule.CleanerPO;
import com.jk.ndt.etl.entity.rule.CleanerSheetPO;
import com.jk.ndt.etl.rule.StringOperation;
import com.jk.ndt.etl.service.rule.CleanService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.SpringContextHolder;
import com.jk.ndt.etl.utility.WebUtils;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;

/**
 * 
 * @ClassName: CleanServiceImpl
 * @Description: 清洗接口业务实现类
 * @author fangwei
 * @date 2017年6月2日 上午9:27:12
 *
 */
@Service("cleanService")
public class CleanServiceImpl implements CleanService {

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SheetInfoDao sheetInfoDao;
    @Autowired
    private SheetLogDao sheetLogDao;

    @Override
    public CleanResult clean(CleanerSheetPO cleanerSheetPO) {
        CleanResult result = new CleanResult();
        Map<String, List<CleanerPO>> cleanerMap = handleCleanMap(cleanerSheetPO);
        List<Map<String, Object>> listAll = commonDao
                .listAll(Constant.ORIGINAL_TABLE_PREFIX + cleanerSheetPO.getSheet_id(), null);
        try {
            // 清洗数据
            List<Map<String, Object>> newData = doClean(listAll, cleanerMap);
            DataTable dataTable = new DataTable(newData, Constant.CLEANED_TABLE_PREFIX + cleanerSheetPO.getSheet_id());
            commonDao.saveTableContentWithId(dataTable);

            // 修改sheet表状态为已清洗
            SheetInfo sheetInfo = new SheetInfo();
            sheetInfo.setId(cleanerSheetPO.getSheet_id());
            sheetInfo.setStatus(Constant.SHEET_STATUS_CLEANED);
            sheetInfoDao.update(sheetInfo);
            result.setIsCleaned(true);

            // 记录日志
            HttpServletRequest request = WebUtils.getRequest();
            SheetLog sheetLog = new SheetLog(sheetInfo, LoginSessionUtil.getAdmin(request), Constant.SHEET_LOG_CLEANED,
                    new Date(), JSON.toJSONString(cleanerSheetPO),
                    Constant.ORIGINAL_TABLE_PREFIX + cleanerSheetPO.getSheet_id(),
                    Constant.CLEANED_TABLE_PREFIX + cleanerSheetPO.getSheet_id());
            sheetLogDao.save(sheetLog);
        } catch (Exception e) {
            result.setIsCleaned(false);
            result.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> operationTest(String inputString, List<CleanerPO> cleanerPOList) {
        List<String> list = new ArrayList<String>();
        // 更具列名获取清洗器列表
        if (Checker.isNotNullOrEmpty(cleanerPOList)) {
            for (CleanerPO cleanerPO : cleanerPOList) {
                String cleanerName = cleanerPO.getCleanerName();
                StringOperation stringOperation = SpringContextHolder.getBean(cleanerName);
                inputString = stringOperation.operate(inputString, cleanerPO.getParams());
                list.add(inputString);
                stringOperation.clean();
            }
        }
        return list;
    }

    /**
     * 
     * @Description: 执行清洗数据操作
     * @author fangwei
     * @date 2017年5月26日 下午3:14:03
     * @param listAll
     * @param cleanerMap
     * @return
     */
    private List<Map<String, Object>> doClean(List<Map<String, Object>> listAll,
            Map<String, List<CleanerPO>> cleanerMap) {
        if (Checker.isNotNullOrEmpty(listAll)) {
            for (Map<String, Object> map : listAll) {
                Set<Entry<String, Object>> rowEntrySet = map.entrySet();
                for (Entry<String, Object> entry : rowEntrySet) {
                    String columnName = entry.getKey();
                    String value = entry.getValue().toString();
                    // 更具列名获取清洗器列表
                    List<CleanerPO> cleanerList = cleanerMap.get(columnName);
                    if (Checker.isNotNullOrEmpty(cleanerList)) {
                        for (CleanerPO cleanerPO : cleanerList) {
                            String cleanerName = cleanerPO.getCleanerName();
                            StringOperation stringOperation = SpringContextHolder.getBean(cleanerName);
                            value = stringOperation.operate(value, cleanerPO.getParams());
                            stringOperation.clean();
                        }
                        map.put(columnName, value);
                    }
                }
            }
        }
        return listAll;
    }

    /**
     * 
     * @Description: 将传递过来的清洗数据封装成key为列表，value为清洗器列表的map
     * @author fangwei
     * @date 2017年5月26日 下午2:51:26
     * @param cleanerSheetPO
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, List<CleanerPO>> handleCleanMap(CleanerSheetPO cleanerSheetPO) {
        SheetInfo sheetInfo = sheetInfoDao.getById(cleanerSheetPO.getSheet_id());
        Map<String, String> columnsMap = null;
        if (Checker.isNotNullOrEmpty(sheetInfo.getColumns())) {
            columnsMap = JSON.parseObject(sheetInfo.getColumns(), Map.class);
        }

        Map<String, List<CleanerPO>> cleanerMap = new HashMap<String, List<CleanerPO>>();
        List<CleanerColumnPO> columns = cleanerSheetPO.getColumns();
        for (CleanerColumnPO cleanerColumnPO : columns) {
            if (Checker.isNotNullOrEmpty(cleanerColumnPO.getOperations())) {
                if (Checker.isNotNullOrEmpty(columnsMap)) {
                    Set<Entry<String, String>> entrySet = columnsMap.entrySet();
                    for (Entry<String, String> entry : entrySet) {
                        if (cleanerColumnPO.getName().trim().equals(entry.getValue().trim())) {
                            cleanerMap.put(entry.getKey().trim(), cleanerColumnPO.getOperations());
                            break;
                        }
                    }
                } else {
                    cleanerMap.put(cleanerColumnPO.getName(), cleanerColumnPO.getOperations());
                }
            }
        }
        return cleanerMap;
    }

}
