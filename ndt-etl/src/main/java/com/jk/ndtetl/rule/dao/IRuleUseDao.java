package com.jk.ndtetl.rule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.rule.RuleUse;

public interface IRuleUseDao extends BaseDao<RuleUse> {

    /**
     * 
     * @Description:
     * @author wangzhi
     * @date 2017年7月7日 上午10:21:25
     * @param filed
     * @return void
     */
    void listRuleByTableIdAndColumnId(ColumnDef filed);

    /**
     * @Description: 映射关系批量插入
     * @author wangzhi
     * @date 2017年7月7日 上午10:21:11
     * @param ruleUse
     * @return void
     */
    void batchSave(List<RuleUse> lists);

    /**
     * @Description:
     * @author wangzhi
     * @date 2017年7月8日 下午2:11:27
     * @param ruleUse
     * @return
     * @return List<RuleUse>
     */
    List<RuleUse> listRuleSuitByTableId(RuleUse ruleUse);

    /**
     * @Description:
     * @author wangzhi
     * @date 2017年7月8日 下午2:49:01
     * @param ruleUseColumnsList
     * @return void
     */
    void deleteRuleUseByTableId(RuleUse ruleUse);

    void deleteByIds(String waitDelIds);

    List<RuleUse> listRuleUseByTables(@Param("sourceTableId")Integer sourceTableId, @Param("targetTableId")Integer targetTableId);

    /**
     * 根据源表和目标表查询所有列的规则
     * 
     * @author lianhanwen
     * @date 2017年7月25日 下午3:30:46
     * @param sourceTableId
     * @param targetTableId
     * @return
     */
    List<RuleUse> getRulesByTable(@Param("sourceTableId")Integer sourceTableId, @Param("targetTableId")Integer targetTableId);
}
