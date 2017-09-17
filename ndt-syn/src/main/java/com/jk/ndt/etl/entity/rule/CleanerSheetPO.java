package com.jk.ndt.etl.entity.rule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: CleanerSheetPO
 * @Description: sheet清洗数据模型，用于封装接受的参数
 * @author fangwei
 * @date 2017年5月26日 下午2:25:51
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class CleanerSheetPO {
    /**
     * 表sheet的id
     */
    private Integer sheet_id;
    /**
     * 列验证器列表
     */
    private List<CleanerColumnPO> columns;

    public Integer getSheet_id() {
        return sheet_id;
    }

    public void setSheet_id(Integer sheet_id) {
        this.sheet_id = sheet_id;
    }

    public List<CleanerColumnPO> getColumns() {
        return columns;
    }

    public void setColumns(List<CleanerColumnPO> columns) {
        this.columns = columns;
    }

}
