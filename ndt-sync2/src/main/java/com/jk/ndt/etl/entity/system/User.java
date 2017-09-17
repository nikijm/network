package com.jk.ndt.etl.entity.system;


import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.entity.BaseEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * created by 朱生 on 2017/5/13.
 * user 为客户使用表，于本系统权限无关。
 */
public class User extends BaseEntity  {

    private BigDecimal org_id;
    private BigDecimal id;
    @Email
    private String email;
    @NotBlank
    private String name;

    private String password;
    @Pattern(regexp = Constant.REG_PHONE,message="手机号格式不正确")
    private String phone;
    private String description;

	public BigDecimal getOrg_id() {
        return org_id;
    }

    public void setOrg_id(BigDecimal org_id) {
        this.org_id = org_id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
