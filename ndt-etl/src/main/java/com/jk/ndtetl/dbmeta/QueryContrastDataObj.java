package com.jk.ndtetl.dbmeta;

import java.util.List;

/**
 * 对比数据的查询条件封装类
 * 
 * @ClassName: QueryContrastDataObj
 * @author lianhanwen
 * @date 2017年7月24日 下午5:05:49
 *
 */
public class QueryContrastDataObj {

    /**
     * 文件的全局唯一编码uuId
     */
    private String unId;

    /**
     * 源表名称
     */
    private String sourceTableName;

    /**
     * 目标表名称
     */
    private String targetTableName;

    /**
     * 源表列名
     */
    private List<String> sourceColumnNames;

    /**
     * 目标表列名
     */
    private List<String> targetColumnNames;

    public String getUnId() {
        return unId;
    }

    public void setUnId(String unId) {
        this.unId = unId;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public List<String> getSourceColumnNames() {
        return sourceColumnNames;
    }

    public void setSourceColumnNames(List<String> sourceColumnNames) {
        this.sourceColumnNames = sourceColumnNames;
    }

    public List<String> getTargetColumnNames() {
        return targetColumnNames;
    }

    public void setTargetColumnNames(List<String> targetColumnNames) {
        this.targetColumnNames = targetColumnNames;
    }

}