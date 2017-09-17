package com.jk.ndt.etl.service.rule;

import java.util.List;

import com.jk.ndt.etl.entity.rule.CleanResult;
import com.jk.ndt.etl.entity.rule.CleanerPO;
import com.jk.ndt.etl.entity.rule.CleanerSheetPO;

/**
 * 
 * @ClassName: CleanService
 * @Description: 清洗接口类
 * @author fangwei
 * @date 2017年6月2日 上午9:16:07
 *
 */
public interface CleanService {
    /**
     * 
     * @Description: 根据清洗规则对sheet数据进行清洗
     * @author fangwei
     * @date 2017年6月2日 上午9:20:43
     * @param cleanerSheetPO
     * @return
     */
    CleanResult clean(CleanerSheetPO cleanerSheetPO);

    /**
     * 
     * @Description: 根据清洗规则对传入的字符串进行处理，并将处理后的值放在list中返回  
     * @author fangwei
     * @date 2017年6月2日 上午11:12:55 
     * @param inputString
     * @param cleanerPOList
     * @return
     */
    List<String> operationTest(String inputString, List<CleanerPO> cleanerPOList);
}
