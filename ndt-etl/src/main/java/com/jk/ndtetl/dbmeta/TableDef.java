package com.jk.ndtetl.dbmeta;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.jk.ndtetl.BaseEntity;

/**
 * @ClassName: TableDef
 * @Description: ETL_TABLE对应的实体类
 * @author lianhanwen
 * @date 2017年6月22日 下午3:10:44
 *
 */

public class TableDef extends BaseEntity {

    // 表类型,数据表用途:cache -> validate ->clean ->convert  ->verity
    public static final String CATEGORY_CACHE = "cache";

    public static final String CATEGORY_VALIDATE = "validate";// 验证--缓存后的验证

    public static final String CATEGORY_CLEAN = "clean";

    public static final String CATEGORY_CONVERT = "convert";

    public static final String CATEGORY_VERIFY = "verify";

    // 表前缀
    public static final String CACHETABLE_PREFIX = "ETL_CACHE_";

    public static final String CLEANTABLE_PREFIX = "ETL_CLEAN_";

    public static final String CONVERTTABLE_PREFIX = "ETL_CONVERT_";

    public static final String VERIFYTABLE_PREFIX = "ETL_VERIFY_";

    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1L;

    /**
     * id,序列递增
     */
    private Integer etlTableId;

    /**
     * 别名
     */
    private String name;

    /**
     * 表名称（物理表名)
     */
    @NotBlank(message = "表名不能为空")
    private String tableName;

    /**
     * 描述
     */
    private String description;

    /**
     * 表类型,数据表用途:cache clean convert validate
     */
    @NotBlank(message = "请选择表类型")
    private String category;

    /**
     * 业务类型
     */
    private BusinessType businessType;

    // 业务类型id
    private Integer etlBusinessTypeId;

    /**
     * 一对多关系,columnDef,表对应的所有列
     */
    private List<ColumnDef> columns;

    // 是否同时创建清洗表 true 是
    private boolean createCleanTable;
    // 是否同时创建校验表 true 是
    private boolean createVerifyTable;

    public boolean isCreateCleanTable() {
        return createCleanTable;
    }

    public void setCreateCleanTable(boolean createCleanTable) {
        this.createCleanTable = createCleanTable;
    }

    public boolean isCreateVerifyTable() {
        return createVerifyTable;
    }

    public void setCreateVerifyTable(boolean createVerifyTable) {
        this.createVerifyTable = createVerifyTable;
    }

    public Integer getEtlBusinessTypeId() {
        return etlBusinessTypeId;
    }

    public void setEtlBusinessTypeId(Integer etlBusinessTypeId) {
        this.etlBusinessTypeId = etlBusinessTypeId;
    }

    public Integer getEtlTableId() {
        return etlTableId;
    }

    public void setEtlTableId(Integer etlTableId) {
        this.etlTableId = etlTableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

}
