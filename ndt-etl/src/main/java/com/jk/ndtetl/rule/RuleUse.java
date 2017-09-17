package com.jk.ndtetl.rule;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.jk.ndtetl.BaseEntity;
import com.jk.ndtetl.dbmeta.ColumnDef;

public class RuleUse extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    // 源表
    @NotNull(message = "请选择源表")
    private Integer etlTableId;

    // 源字段
    private Integer etlColumnId;

    // 源字段名
    private String columnName = null;

    // //源表字段
//    private ColumnDef column;

    // 目标表
    @NotNull(message = "请选择目标表")
    private Integer etlTableTargetId;

    // 目标字段
    private Integer etlColumnTargetId;

    private String etlCoulumnTargetName = null;

    // //目标表字段
    // private ColumnDef targetColumns;
    // 规则模版主键
    private RuleSuit ruleSuit;

    // 顺序号
    private Integer seqNo;

    // 源表字段：目标表字段：模版id
    @NotNull(message = "请选择规则模版")
    private List<RuleUse> ruleUseColumnsList;

    // 规则模版主键
    private Integer ruleSuitId;

    /**
     * 本条规则映射对应的某种ETL操作: CACHE：缓存入库 CLEAN:清洗 CONVERT:转换 VALIDATE:验证
     */
    @NotNull(message = "请选择源表类型")
    private String etlOp;
    // 源表字段：目标表字段：模版id

    public Integer getEtlTableTargetId() {
        return etlTableTargetId;
    }

    public void setEtlTableTargetId(Integer etlTableTargetId) {
        this.etlTableTargetId = etlTableTargetId;
    }

    public Integer getEtlColumnTargetId() {
        return etlColumnTargetId;
    }

    public void setEtlColumnTargetId(Integer etlColumnTargetId) {
        this.etlColumnTargetId = etlColumnTargetId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEtlTableId() {
        return etlTableId;
    }

    public void setEtlTableId(Integer etlTableId) {
        this.etlTableId = etlTableId;
    }

    public Integer getEtlColumnId() {
        return etlColumnId;
    }

    public void setEtlColumnId(Integer etlColumnId) {
        this.etlColumnId = etlColumnId;
    }

    public RuleSuit getRuleSuit() {
        return ruleSuit;
    }

    public void setRuleSuit(RuleSuit ruleSuit) {
        this.ruleSuit = ruleSuit;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public List<RuleUse> getRuleUseColumnsList() {
        return ruleUseColumnsList;
    }

    public String getEtlOp() {
        return etlOp;
    }

    public void setEtlOp(String etlOp) {
        this.etlOp = etlOp;
    }

    public void setRuleUseColumnsList(List<RuleUse> ruleUseColumnsList) {
        this.ruleUseColumnsList = ruleUseColumnsList;
    }

    public Integer getRuleSuitId() {
        return ruleSuitId;
    }

    public void setRuleSuitId(Integer ruleSuitId) {
        this.ruleSuitId = ruleSuitId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getEtlCoulumnTargetName() {
        return etlCoulumnTargetName;
    }

    public void setEtlCoulumnTargetName(String etlCoulumnTargetName) {
        this.etlCoulumnTargetName = etlCoulumnTargetName;
    }

//    public ColumnDef getColumn() {
//        return column;
//    }
//
//    public void setColumn(ColumnDef column) {
//        this.column = column;
//    }

}
