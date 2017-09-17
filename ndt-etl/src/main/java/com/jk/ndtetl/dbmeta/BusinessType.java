package com.jk.ndtetl.dbmeta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jk.ndtetl.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessType extends BaseEntity {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键id,序列递增
     */
    private Integer etlBusinessTypeId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    public Integer getEtlBusinessTypeId() {
        return etlBusinessTypeId;
    }

    public void setEtlBusinessTypeId(Integer etlBusinessTypeId) {
        this.etlBusinessTypeId = etlBusinessTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
