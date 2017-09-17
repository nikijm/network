package com.jk.ndt.etl.entity.rule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: ValidateColumnPO
 * @Description: sheet验证数据模型
 * @author fangwei
 * @date 2017年5月24日 下午4:47:29
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class ValidateSheetPO {
    /**
     * 表sheet的id
     */
    private Integer sheet_id;
    /**
     * 列验证器列表
     */
    private List<ValidateColumnPO> columns;

    public Integer getSheet_id() {
        return sheet_id;
    }

    public void setSheet_id(Integer sheet_id) {
        this.sheet_id = sheet_id;
    }

    public List<ValidateColumnPO> getColumns() {
        return columns;
    }

    public void setColumns(List<ValidateColumnPO> columns) {
        this.columns = columns;
    }

}
