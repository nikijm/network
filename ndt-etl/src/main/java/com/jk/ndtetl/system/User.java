package com.jk.ndtetl.system;


import java.lang.Integer;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.jk.ndtetl.BaseSystemEntity;

/**
 * created by 朱生 on 2017/5/13.
 * user 为客户使用表，于本系统权限无关。
 */
public class User extends BaseSystemEntity {

    //    用户id
    private Integer id;

    //    机构id
    private Integer orgId;

    //    用户名
    @NotBlank(message = "用户名不能为空")
    private String name;

    //    密码
    @Pattern(regexp = User.REG_PASSWORD, message = PWD_NOTICE)
    private String password;

    //邮箱
    @Email(message = "邮箱格式不正确")
    private String email;

    //电话
    @Pattern(regexp = User.REG_PHONE, message = "手机号格式不正确")
    private String phone;

    //电话2
    @Pattern(regexp = User.REG_PHONE, message = "手机号2格式不正确")
    private String phone2;

    //描述
    private String description;

    //是否允许删除
    private String isdelete;

    //关系表
    private List<Role> roles;//角色
    private Integer[] roleIds;//角色id

    //机构名
    private String orgName;
    //是否只查询外部用户
    private String externalz;


    /**
     * 密码提示信息
     */
    public static final String PWD_NOTICE = "请输入9-20位字母数字,可选特殊符号#@!~%^&*-+，空格单双引号除外";

    // ######################### 校验规则常量 #########################
    /**
     * 电话
     */
    public static final String REG_PHONE = "^$|^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0])|(18[0,5-9]))\\d{8}$";
    /**
     * 座机
     */
    public static final String LANDLINE = "^0\\d{2,3}-\\d{7,8}(-\\d{1,6})?$";
    /**
     * 邮箱
     */
    public static final String REG_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 密码
     */
    public static final String REG_PASSWORD = "^((?=.*[a-z])(?=.*\\d)|(?=[a-z])(?=.*[#@!~%^&*\\-+])|(?=.*\\d)(?=.*[#@!~%^&*\\-+]))[a-zA-Z\\d#@!~%^&*\\-+]{9,20}$";


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Integer[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Integer[] roleIds) {
        this.roleIds = roleIds;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer etl_org_id) {
        this.orgId = etl_org_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getExternalz() {
        return externalz;
    }

    public void setExternalz(String externalz) {
        this.externalz = externalz;
    }
}
