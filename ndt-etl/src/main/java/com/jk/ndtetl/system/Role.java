package com.jk.ndtetl.system;

import java.math.BigDecimal;
import java.util.List;

import com.jk.ndtetl.BaseSystemEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by 朱生 on 2017/5/18.
 */
public class Role extends BaseSystemEntity {

    private Integer id;

    @NotBlank(message = "角色名不能为空")
    private String name;
    
    private String description;
    
    private String permissions;//存储所选权限(资源表)的json数据
    
    private String isdelete;

    //关系表
    private List<Resource> resources;

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

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

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }
}
