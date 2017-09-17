package com.jk.ndt.etl.entity.rule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: TransformResult
 * @Description: 转换结果对象封装
 * @author fangwei
 * @date 2017年6月2日 上午9:56:24
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class TransformResult {
    /**
     * 转换是否成功
     */
    private Boolean isTransformed;

    public Boolean getIsTransformed() {
        return isTransformed;
    }

    public void setIsTransformed(Boolean isTransformed) {
        this.isTransformed = isTransformed;
    }

}
