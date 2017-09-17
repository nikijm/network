package com.jk.ndt.etl.service.rule.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.dao.access.SheetInfoDao;
import com.jk.ndt.etl.dao.common.CommonDao;
import com.jk.ndt.etl.dao.logs.SheetLogDao;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.entity.rule.ValidateColumnPO;
import com.jk.ndt.etl.entity.rule.ValidatePO;
import com.jk.ndt.etl.entity.rule.ValidateResult;
import com.jk.ndt.etl.entity.rule.ValidateSheetPO;
import com.jk.ndt.etl.rule.ValidateUtils;
import com.jk.ndt.etl.rule.Validator;
import com.jk.ndt.etl.service.rule.ValidateService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.SpringContextHolder;
import com.jk.ndt.etl.utility.WebUtils;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;

/**
 * 
 * @ClassName: ValidateServiceImpl
 * @Description: validate业务实现类
 * @author fangwei
 * @date 2017年6月2日 上午9:07:21
 *
 */
@Service("validateService")
public class ValidateServiceImpl implements ValidateService {

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SheetInfoDao sheetInfoDao;
    @Autowired
    private SheetLogDao sheetLogDao;

    @SuppressWarnings("unchecked")
    @Override
    public ValidateResult validate(ValidateSheetPO validateSheetPO) {
        // 查询sheet表中列名和文件表头中文对应关系
        SheetInfo sheetInfo = sheetInfoDao.getById(validateSheetPO.getSheet_id());
        Map<String, String> columnsMap = null;
        if (sheetInfo != null && Checker.isNotNullOrEmpty(sheetInfo.getColumns())) {
            columnsMap = JSON.parseObject(sheetInfo.getColumns(), Map.class);
        }
        Map<String, ValidatePO> validateMap = handleValidateMap(validateSheetPO, columnsMap);
        List<Map<String, Object>> listAll = commonDao
                .listAll(Constant.ORIGINAL_TABLE_PREFIX + validateSheetPO.getSheet_id(), null);
        ValidateResult result = doValidate(validateMap, listAll, columnsMap);
        if (result.getIsValidate()) {
            // 修改sheet表状态为已验证
            sheetInfo = new SheetInfo();
            sheetInfo.setId(validateSheetPO.getSheet_id());
            sheetInfo.setStatus(Constant.SHEET_STATUS_VALIDATED);
            sheetInfoDao.update(sheetInfo);
            // 记录日志
            HttpServletRequest request = WebUtils.getRequest();
            SheetLog sheetLog = new SheetLog(sheetInfo, LoginSessionUtil.getAdmin(request),
                    Constant.SHEET_LOG_VALIDATED, new Date(), JSON.toJSONString(validateSheetPO),
                    Constant.ORIGINAL_TABLE_PREFIX + validateSheetPO.getSheet_id(),
                    Constant.ORIGINAL_TABLE_PREFIX + validateSheetPO.getSheet_id());
            sheetLogDao.save(sheetLog);

        }
        return result;
    }

    /**
     * 
     * @Description: 对数据的验证
     * @author fangwei
     * @date 2017年5月27日 上午9:35:31
     * @param validateMap
     * @param listAll
     * @return
     */
    private ValidateResult doValidate(Map<String, ValidatePO> validateMap, List<Map<String, Object>> listAll,
            Map<String, String> columnsMap) {
        ValidateResult result = new ValidateResult();
        result.setIsValidate(true);

        // 验证数据格式
        if (Checker.isNotNullOrEmpty(listAll)) {
            List<Map<String, Object>> error = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> map : listAll) {
                Set<Entry<String, Object>> entrySet = map.entrySet();
                Map<String, Object> errorColumnMap = new HashMap<String, Object>();
                for (Entry<String, Object> entry : entrySet) {
                    // 校验值
                    Object value = entry.getValue();
                    // 校验key
                    String key = entry.getKey().trim();
                    ValidatePO validatePO = validateMap.get(key);
                    if (validatePO != null) {
                        if (!validateValue(validatePO, value)) {
                            result.setIsValidate(false);
                            if (Checker.isNotNullOrEmpty(columnsMap)) {
                                Set<Entry<String, String>> cloumnsMapEntrySet = columnsMap.entrySet();
                                boolean flag = false;
                                for (Entry<String, String> cloumnsMapEntry : cloumnsMapEntrySet) {
                                    if (key.equals(cloumnsMapEntry.getKey().trim()) && Checker.isNotNullOrEmpty(cloumnsMapEntry.getValue().trim())) {
                                        errorColumnMap.put(cloumnsMapEntry.getValue().trim(), value);
                                        flag = true;
                                        break;
                                    }
                                }
                                if(!flag){
                                    errorColumnMap.put(key, value);
                                }
                            }
                        }
                    }
                }
                if (Checker.isNotNullOrEmpty(errorColumnMap)) {
                    Map<String, Object> errorRowMap = new HashMap<String, Object>();
                    errorRowMap.put("ID", map.get("ID"));
                    errorRowMap.put("columns", errorColumnMap);
                    error.add(errorRowMap);
                }
            }
            if (Checker.isNotNullOrEmpty(error)) {
                result.setRows(error);
            }
        }
        return result;
    }

    /**
     * 
     * @Description: 验证具体的值
     * @author fangwei
     * @date 2017年5月25日 上午10:59:00
     * @param validatePO
     * @param value
     * @return
     */
    private boolean validateValue(ValidatePO validatePO, Object value) {
        Validator validator = null;
        if (Checker.isNullOrEmpty(validatePO.getValidatorName())) {
            String validatorName = ValidateUtils.findValidatorNameByName(validatePO.getName());
            if (Checker.isNullOrEmpty(validatorName)) {
                return true;
            } else {
                validatePO.setValidatorName(validatorName);
            }
        }
        try {
            validator = SpringContextHolder.getBean(validatePO.getValidatorName());
            if (Checker.isNullOrEmpty(validatePO.getParams())) {
                String defaultValue = ValidateUtils.findDefaultValutByName(validatePO.getName());
                if (Checker.isNotNullOrEmpty(defaultValue)) {
                    List<Object> params = new ArrayList<Object>();
                    params.add(defaultValue);
                    validatePO.setParams(params);
                }
            }
            boolean result = validator.validate(value.toString(), validatePO.getParams());
            return result;
        } finally {
            validator.clean();
        }
    }

    /**
     * 
     * @Description: 将表单验证对象转换成列为key，验证器为value的map
     * @author fangwei
     * @date 2017年5月25日 上午10:40:49
     * @param validateSheetPO
     * @return
     */
    private Map<String, ValidatePO> handleValidateMap(ValidateSheetPO validateSheetPO, Map<String, String> columnsMap) {
        Map<String, ValidatePO> validateMap = new HashMap<String, ValidatePO>();
        List<ValidateColumnPO> columns = validateSheetPO.getColumns();
        for (ValidateColumnPO validateColumnPO : columns) {
            if (Checker.isNotNullOrEmpty(validateColumnPO.getValidator())) {
                if (Checker.isNotNullOrEmpty(columnsMap)) {
                    Set<Entry<String, String>> entrySet = columnsMap.entrySet();
                    boolean flag = false;
                    for (Entry<String, String> entry : entrySet) {
                        if (validateColumnPO.getName().trim().equals(entry.getValue().trim())) {
                            validateMap.put(entry.getKey().trim(), validateColumnPO.getValidator());
                            flag = true;
                            break;
                        }
                    }
                    //没有找到对应的中文,就使用英文的
                    if(!flag){
                        validateMap.put(validateColumnPO.getName(), validateColumnPO.getValidator());
                    }
                } else {
                    validateMap.put(validateColumnPO.getName(), validateColumnPO.getValidator());
                }
            }
        }
        return validateMap;
    }

}
