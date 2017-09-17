package com.jk.ndt.etl.entity.system;

import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.entity.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 朱生 on 2017/5/22.
 */
public class Admin extends BaseEntity implements Serializable{

    private List<Role> roles;//角色
    private BigDecimal[] roleIds;//角色id

    private BigDecimal id;
    @NotBlank
    private String name;
    @Pattern(regexp = Constant.REG_EMAIL,message="邮箱格式不正确")
    private String email;

    private String password;
    @Pattern(regexp = Constant.REG_PHONE,message="手机号格式不正确")
    private String phone;
    private Boolean notdelete;
    
    public Boolean getNotdelete() {
        return notdelete;
    }

    public void setNotdelete(Boolean notdelete) {
        this.notdelete = notdelete;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public BigDecimal[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(BigDecimal[] roleIds) {
        this.roleIds = roleIds;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
