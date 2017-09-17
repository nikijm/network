package com.jk.ndt.etl.service.rule;

import com.jk.ndt.etl.entity.rule.TransformResult;
import com.jk.ndt.etl.entity.rule.TransformSheetPO;

/**
 * 
 * @ClassName: TransformService
 * @Description: 转换器服务类
 * @author fangwei
 * @date 2017年5月31日 下午6:06:55
 *
 */
public interface TransformService {
    /**
     * 
     * @Description: 将数据进行转换映射处理
     * @author fangwei
     * @date 2017年6月2日 上午9:58:17
     * @param transformSheetPO
     * @return
     */
    TransformResult transform(TransformSheetPO transformSheetPO);
}
