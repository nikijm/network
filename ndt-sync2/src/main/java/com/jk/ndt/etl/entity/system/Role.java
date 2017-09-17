package com.jk.ndt.etl.entity.system;

import com.jk.ndt.etl.entity.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/18.
 */
public class Role extends BaseEntity{

    private BigDecimal id;

    private String name;
    private String description;
    private String permissions;//存储所选权限(资源表)的json数据
    private Boolean notdelete;

    public Boolean getNotdelete() {
        return notdelete;
    }

    public void setNotdelete(Boolean notdelete) {
        this.notdelete = notdelete;
    }

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

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
