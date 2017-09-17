package com.jk.ndtetl.rule.service.impl;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.dao.DataFileDao;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.rule.RuleUse;
import com.jk.ndtetl.rule.dao.IRuleUseDao;
import com.jk.ndtetl.rule.service.IRuleUseService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * 
 * @ClassName: RuleUseService
 * @Description:
 * @author wangzhi
 * @date 2017年7月3日 下午2:10:29
 *
 */
@Service("iRuleUseService")
public class RuleUseServiceImpl extends BaseServiceImpl<RuleUse> implements IRuleUseService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRuleUseDao ruleUseDao;
    @Autowired
    private DataFileDao dataFileDao;

    @Override
    protected BaseDao<RuleUse> getDao() {

        return ruleUseDao;
    }

    @Override
    public void batchSave(List<RuleUse> ruleUseColumnsList,String waitDelIds) {
        if (!StringUtils.isBlank(waitDelIds)) {
            ruleUseDao.deleteByIds(waitDelIds);
        }
        if (Checker.isNotNullOrEmpty(ruleUseColumnsList)) {
            ruleUseDao.batchSave(ruleUseColumnsList);
        }
    }

    @Override
    public void updateRuleUseByTableId(RuleUse ruleUse) {
        if (ruleUse != null && ruleUse.getRuleUseColumnsList() != null) {
            ruleUseDao.deleteRuleUseByTableId(ruleUse);
            ruleUseDao.batchSave(ruleUse.getRuleUseColumnsList());
        }
        else {
            logger.info("参数有误");
        }
    }

    @Override
    public List<RuleUse> listRuleSuitByTableId(RuleUse ruleUse) {
        List<RuleUse> lists = ruleUseDao.listRuleSuitByTableId(ruleUse);
        return lists;
    }

    @Override
    public List<RuleUse> listRuleUseByTables(Integer sourceTableId, Integer targetTableId) {
        List<RuleUse> lists = ruleUseDao.listRuleUseByTables(sourceTableId, targetTableId);
        return lists;
    }

    @Override
    public List<RuleUse> getRulesByFile(Integer dataFileId, String category) throws CustomException {
        
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
            return ruleUseDao.getRulesByTable(cacheTableId, cleanTableId);
        }
        if (TableDef.CATEGORY_CONVERT.equals(category)) {
            return ruleUseDao.getRulesByTable(cleanTableId, convertTableId);
        }
        if (TableDef.CATEGORY_VERIFY.equals(category)) {
            return ruleUseDao.getRulesByTable(convertTableId, validateTableId);
        }
        throw new CustomException("当前处理还没有相应的规则");
    }

}
