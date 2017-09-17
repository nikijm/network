package com.jk.ndtetl.dbmeta;

import com.jk.ndtetl.BaseEntity;
import com.jk.ndtetl.etl.Org;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class DataFileType extends BaseEntity {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键id,序列递增
     */
    private Integer etlFileTypeId;

    /**
     * 组织id
     */
    @NotNull(message = "请选择机构")
    private Integer etlOrgId;

    /**
     * 组织机构
     */
    private Org org;

    /**
     * 名称
     */
    @NotBlank(message = "业务名不能为空")
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 缓存表的id
     */
    @NotNull(message = "请选择缓存后存入表")
    private Integer tableCacheId;

    /**
     * 缓存表
     */
    private TableDef tableCache;

    /**
     * 清洗表
     */
    private TableDef tableClean;

    /**
     * 清洗表的id
     */
    @NotNull(message = "请选择清洗后存入表")
    private Integer tableCleanId;

    /**
     * 转换表
     */
    private TableDef tableConvert;

    /**
     * 转换表的id
     */
    @NotNull(message = "请选择转换后存入表")
    private Integer tableConvertId;

    /**
     * 校验表
     */
    private TableDef tableValidate;

    /**
     * 校验表的id
     */
    @NotNull(message = "请选择校验后存入表")
    private Integer tableValidateId;

    /**
     * 数据文件与缓存表字段对齐
     */
    private String fieldsAlign;

    public Integer getEtlFileTypeId() {
        return etlFileTypeId;
    }

    public void setEtlFileTypeId(Integer etlFileTypeId) {
        this.etlFileTypeId = etlFileTypeId;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
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

    public TableDef getTableCache() {
        return tableCache;
    }

    public void setTableCache(TableDef tableCache) {
        this.tableCache = tableCache;
    }

    public TableDef getTableClean() {
        return tableClean;
    }

    public void setTableClean(TableDef tableClean) {
        this.tableClean = tableClean;
    }

    public TableDef getTableConvert() {
        return tableConvert;
    }

    public void setTableConvert(TableDef tableConvert) {
        this.tableConvert = tableConvert;
    }

    public TableDef getTableValidate() {
        return tableValidate;
    }

    public void setTableValidate(TableDef tableValidate) {
        this.tableValidate = tableValidate;
    }

    public String getFieldsAlign() {
        return fieldsAlign;
    }

    public void setFieldsAlign(String fieldsAlign) {
        this.fieldsAlign = fieldsAlign;
    }

    public Integer getEtlOrgId() {
        return etlOrgId;
    }

    public void setEtlOrgId(Integer etlOrgId) {
        this.etlOrgId = etlOrgId;
    }

    public Integer getTableCacheId() {
        return tableCacheId;
    }

    public void setTableCacheId(Integer tableCacheId) {
        this.tableCacheId = tableCacheId;
    }

    public Integer getTableCleanId() {
        return tableCleanId;
    }

    public void setTableCleanId(Integer tableCleanId) {
        this.tableCleanId = tableCleanId;
    }

    public Integer getTableConvertId() {
        return tableConvertId;
    }

    public void setTableConvertId(Integer tableConvertId) {
        this.tableConvertId = tableConvertId;
    }

    public Integer getTableValidateId() {
        return tableValidateId;
    }

    public void setTableValidateId(Integer tableValidateId) {
        this.tableValidateId = tableValidateId;
    }
}
