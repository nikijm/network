package com.jk.ndtetl.system;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotBlank;

import com.jk.ndtetl.BaseSystemEntity;

/**
 * Created by 朱生 on 2017/5/22.
 */
public class Resource extends BaseSystemEntity {

    private Integer id;
    @NotBlank(message = "资源名不能为空")
    private String name;
    private String description;
    @NotBlank(message = "资源编码不能为空")
    private String key;
    @NotBlank(message = "资源可用操作不能为空")
    private String availableOperations;//可用操作一个空格隔开,统一小写 c u r d

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getAvailableOperations() {
        return availableOperations;
    }

    public void setAvailableOperations(String availableOperations) {
        this.availableOperations = availableOperations;
    }
}
