package com.jk.ndtetl.rule.service;

import java.util.List;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.rule.RuleUse;

/**
 * 
 * 
 * @ClassName: IRuleUseService
 * @Description:
 * @author wangzhi
 * @date 2017年7月3日 下午2:12:14
 *
 */
public interface IRuleUseService extends IBaseService<RuleUse> {

    /**
     * 
     * @Description: 批量保存
     * @author wangzhi
     * @date 2017年7月8日 上午11:05:18
     * @param ruleUseColumnsList
     * @param waitDelIds
     * @return void
     */
    public void batchSave(List<RuleUse> ruleUseColumnsList,String waitDelIds);

    /**
     * 
     * @Description: 修改表的映射规则
     * @author wangzhi
     * @date 2017年7月8日 上午11:05:47
     * @param tableId
     * @return void
     */
    public void updateRuleUseByTableId(RuleUse ruleUse);

    /**
     * 
     * @@author wangzhi Description: 通过表名查规则映射
     * @date 2017年7月8日 下午1:57:48
     * @param tableId
     * @return
     * @return List<RuleUse>
     */
    public List<RuleUse> listRuleSuitByTableId(RuleUse ruleUse);

    public List<RuleUse> listRuleUseByTables(Integer sourceTableId, Integer targetTableId);

    /**
     * 根据文件id查询所有列的规则
     * 
     * @author lianhanwen
     * @date 2017年7月25日 下午3:34:40
     * @param sourceTableId
     * @param targetTableId
     * @return
     * @throws CustomException 
     */
    List<RuleUse> getRulesByFile(Integer dataFileId, String category) throws CustomException;

}
