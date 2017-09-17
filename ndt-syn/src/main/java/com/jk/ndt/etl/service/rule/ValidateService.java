package com.jk.ndt.etl.service.rule;

import com.jk.ndt.etl.entity.rule.ValidateResult;
import com.jk.ndt.etl.entity.rule.ValidateSheetPO;

/**
 * 
 * @ClassName: ValidateService
 * @Description: 验证的service接口
 * @author fangwei
 * @date 2017年6月2日 上午8:44:28
 *
 */
public interface ValidateService {
    /**
     * 
     * @Description: 根据验证对象验证sheet中的内容
     * @author fangwei
     * @date 2017年6月2日 上午8:51:56
     * @param validateSheetPO
     * @return
     */
    ValidateResult validate(ValidateSheetPO validateSheetPO);
}
