package com.jk.ndt.etl.entity.system;

import com.jk.ndt.etl.entity.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 朱生 on 2017/5/22.
 */
public class Resource extends BaseEntity implements Serializable{

    private BigDecimal id;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String key;
    @NotBlank
    private String available_operations;//可用操作一个空格隔开,统一小写 c u r d

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAvailable_operations() {
        return available_operations;
    }

    public void setAvailable_operations(String available_operations) {
        this.available_operations = available_operations;
    }
}
