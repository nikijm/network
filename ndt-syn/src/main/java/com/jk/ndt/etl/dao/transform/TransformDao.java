package com.jk.ndt.etl.dao.transform;

import com.jk.ndt.etl.entity.rule.TransformSheetPO;

/**
 * 
 * @ClassName: TransformDao
 * @Description: 转换操作处理类
 * @author fangwei
 * @date 2017年5月31日 下午5:35:20
 *
 */
public interface TransformDao {
    int saveTransformData(TransformSheetPO transformSheetPO);
}
