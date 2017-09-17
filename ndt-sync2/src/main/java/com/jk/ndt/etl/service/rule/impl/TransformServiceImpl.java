package com.jk.ndt.etl.service.rule.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.dao.access.SheetInfoDao;
import com.jk.ndt.etl.dao.common.CommonDao;
import com.jk.ndt.etl.dao.logs.SheetLogDao;
import com.jk.ndt.etl.dao.transform.TransformDao;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.entity.rule.TransformColumnPO;
import com.jk.ndt.etl.entity.rule.TransformResult;
import com.jk.ndt.etl.entity.rule.TransformSheetPO;
import com.jk.ndt.etl.service.rule.TransformService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.WebUtils;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;

/**
 * 
 * @ClassName: TransformServiceImpl
 * @Description: 转换器接口实现类
 * @author fangwei
 * @date 2017年5月31日 下午6:09:53
 *
 */
@Service("transformService")
public class TransformServiceImpl implements TransformService {

    protected Logger logger = LoggerFactory.getLogger(TransformServiceImpl.class);

    @Autowired
    private TransformDao transformDao;
    @Autowired
    private SheetInfoDao sheetInfoDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SheetLogDao sheetLogDao;

    @Override
    public TransformResult transform(TransformSheetPO transformSheetPO) {
        TransformResult result = new TransformResult();
        transformSheetPO.setFromName(Constant.CLEANED_TABLE_PREFIX + transformSheetPO.getSheetId());
        transformSheetPO.setTarget(Constant.TRANSFORMED_TABLE_PREFIX + transformSheetPO.getSheetId());
        // 创建表（列名+表名）
        doCreateTransformTable(transformSheetPO);
        // 转换数据
        doTransform(transformSheetPO);
        // 保存转换数据
        int count = transformDao.saveTransformData(transformSheetPO);
        if (count > 0) {
            result.setIsTransformed(true);

            // 修改sheet表状态为已验证
            SheetInfo sheetInfo = new SheetInfo();
            sheetInfo.setId(transformSheetPO.getSheetId());
            sheetInfo.setStatus(Constant.SHEET_STATUS_TRANSFORMED);
            sheetInfoDao.update(sheetInfo);

            // 记录日志
            HttpServletRequest request = WebUtils.getRequest();
            SheetLog sheetLog = new SheetLog(sheetInfo, LoginSessionUtil.getAdmin(request),
                    Constant.SHEET_LOG_TRANSFORMED, new Date(), JSON.toJSONString(transformSheetPO),
                    Constant.CLEANED_TABLE_PREFIX + transformSheetPO.getSheetId(),
                    Constant.TRANSFORMED_TABLE_PREFIX + transformSheetPO.getSheetId());
            sheetLogDao.save(sheetLog);
        } else {
            result.setIsTransformed(false);
        }
        return result;
    }

    /**
     * 
     * @Description: 转换操作
     * @author fangwei
     * @date 2017年5月31日 下午5:08:37
     * @param transformSheetPO
     * @return
     */
    @SuppressWarnings("unchecked")
    private boolean doTransform(TransformSheetPO transformSheetPO) {
        // 根据中文列名查找对应的真正列名
        SheetInfo sheetInfo = sheetInfoDao.getById(transformSheetPO.getSheetId());
        Map<String, String> columnsMap = null;
        if (Checker.isNotNullOrEmpty(sheetInfo.getColumns())) {
            columnsMap = JSON.parseObject(sheetInfo.getColumns(), Map.class);
        }
        List<TransformColumnPO> columns = transformSheetPO.getColumns();
        for (TransformColumnPO transformColumnPO : columns) {
            if (Checker.isNotNullOrEmpty(columnsMap)) {
                Set<Entry<String, String>> entrySet = columnsMap.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    if (transformColumnPO.getFrom().trim().equals(entry.getValue().trim())) {
                        transformColumnPO.setFromColumnName(entry.getKey().trim());
                    }
                }
            } else {
                transformColumnPO.setFromColumnName(transformColumnPO.getFrom());
            }
        }
        return false;
    }

    /**
     * 
     * @Description: 根据转换对象创建转换表
     * @author fangwei
     * @date 2017年5月31日 下午5:00:40
     * @param transformSheetPO
     */
    private void doCreateTransformTable(TransformSheetPO transformSheetPO) {
        DataTable table = new DataTable();
        table.setTableName(transformSheetPO.getTarget());
        List<TransformColumnPO> columns = transformSheetPO.getColumns();
        if (Checker.isNotNullOrEmpty(columns)) {
            List<String> headers = new ArrayList<String>();
            for (TransformColumnPO transformColumnPO : columns) {
                headers.add(transformColumnPO.getName());
            }
            table.setHeader(headers);
            try {
                commonDao.createTable(table);
                commonDao.createSeq(table.getTableName());
            } catch (Exception e) {
                logger.error("创建表失败");
                commonDao.deleteTable(table.getTableName());
                commonDao.deleteSeq(table.getTableName());
                commonDao.createTable(table);
                commonDao.createSeq(table.getTableName());
            }
        }
    }

}
