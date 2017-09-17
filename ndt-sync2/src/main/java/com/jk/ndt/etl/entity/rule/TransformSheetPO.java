package com.jk.ndt.etl.entity.rule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: TransformPO
 * @Description: 转换器模型类
 * @author fangwei
 * @date 2017年5月31日 下午4:35:10
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class TransformSheetPO {
    /**
     * 对应处理的sheet的id
     */
    private Integer sheetId;
    /**
     * 目标表名
     */
    private String target;
    /**
     * 源表名
     */
    private String fromName;
    /**
     * 转换的列对象列表
     */
    private List<TransformColumnPO> columns;

    public Integer getSheetId() {
        return sheetId;
    }

    public void setSheetId(Integer sheetId) {
        this.sheetId = sheetId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<TransformColumnPO> getColumns() {
        return columns;
    }

    public void setColumns(List<TransformColumnPO> columns) {
        this.columns = columns;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

}
