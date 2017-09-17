package com.jk.ndtetl.dbmeta;

import org.hibernate.validator.constraints.NotBlank;

import com.jk.ndtetl.BaseEntity;
import com.jk.ndtetl.rule.RuleUse;

/**
 * @ClassName: ColumnDef
 * @Description: ETL_COLUMN对应的实体类
 * @author lianhanwen
 * @date 2017年6月22日 下午3:10:39
 *
 */
public class ColumnDef extends BaseEntity implements Comparable<ColumnDef> {

    /**
     * 列类型
     */
    public static final String COLUMN_TYPE_NUMBER = "NUMBER";

    public static final String COLUMN_TYPE_TIMESTAMP = "DATE";

    public static final String COLUMN_TYPE_NVARCHAR2 = "NVARCHAR2";

    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1L;

    /**
     * id,序列递增
     */
    private Integer etlColumnId;

    /**
     * tabledef 主键
     */
    private Integer tableDefId;

    /**
     * 多对一关系,tabledef
     */
    private TableDef tableDef;

    /**
     * 列名
     */
    @NotBlank(message = "列名不能为空")
    private String columnName;

    /**
     * 列值
     */
    private String columnValue;

    /**
     * 列类型
     */
    @NotBlank(message = "列类型名不能为空")
    private String dataType;

    /**
     * 字段长度
     */
    @NotBlank(message = "列长度不能为空")
    private Integer fieldLength;

    /**
     * 别名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 序号
     */
    private Integer seqNo;

    /**
     * 规则映射
     */
    private RuleUse ruleUse;

    /**
     * 是否定为模糊查询字段
     */
    private Character isSearch;

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    public Character getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(Character isSearch) {
        this.isSearch = isSearch;
    }

    public RuleUse getRuleUse() {
        return ruleUse;
    }

    public void setRuleUse(RuleUse ruleUse) {
        this.ruleUse = ruleUse;
    }

    public Integer getEtlColumnId() {
        return etlColumnId;
    }

    public void setEtlColumnId(Integer etlColumnId) {
        this.etlColumnId = etlColumnId;
    }

    public TableDef getTableDef() {
        return tableDef;
    }

    public void setTableDef(TableDef tableDef) {
        this.tableDef = tableDef;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getTableDefId() {
        return tableDefId;
    }

    public void setTableDefId(Integer tableDefId) {
        this.tableDefId = tableDefId;
    }

    @Override
    public int compareTo(ColumnDef o) {
        int i = this.getSeqNo() - o.getSeqNo();// 先按排序字段
        if (i == 0) {
            return this.getEtlColumnId() - o.getEtlColumnId();// 如果排序字段相等再用id进行排序
        }
        return i;
    }
}