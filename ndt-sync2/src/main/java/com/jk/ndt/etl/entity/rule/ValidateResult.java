package com.jk.ndt.etl.entity.rule;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: ValidateResult
 * @Description: 验证返回结果对象
 * @author fangwei
 * @date 2017年6月2日 上午8:47:15
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class ValidateResult {
    /**
     * 是否验证成功
     */
    private Boolean isValidate;
    /**
     * 错误信息map，key为列名
     */
    private List<Map<String, Object>> rows;

    public Boolean getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(Boolean isValidate) {
        this.isValidate = isValidate;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

}
