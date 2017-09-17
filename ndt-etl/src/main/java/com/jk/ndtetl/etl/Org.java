package com.jk.ndtetl.etl;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by 朱生 on 2017/6/9.
 */
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.system.User;

/**
 * 朱生
 *
 * @create 2017-06-09 13:50
 **/
public class Org extends BaseSystemEntity {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    //机构id
    private Integer orgId;

    //机构代码
    @NotBlank(message = "组织机构代码不能为空")
    private String code;

    //机构名
    @NotBlank(message = "组织机构名不能为空")
    private String name;

    //    机构描述
    private String description;

    //机构地址
    private String address;

    //机构电话1
    @Pattern(regexp = User.REG_PHONE, message = "机构手机号1格式不正确")
    private String phone;

    //机构电话2
    private String phone2;

    //机构邮箱
    @Pattern(regexp = User.REG_EMAIL, message = "邮箱格式不正确")
    private String email;

    //机构联系人
    private String attn;

    //机构联系人电话1
    @Pattern(regexp = User.REG_PHONE, message = "联系人手机号1格式不正确")
    private String attnPhone;

    //机构联系人电话2
    @Pattern(regexp = User.REG_PHONE, message = "联系人手机号2格式不正确")
    private String attnPhone2;

    //机构联系人邮箱
    @Pattern(regexp = User.REG_EMAIL, message = "邮箱格式不正确")
    private String attnEmail;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    public String getAttnPhone() {
        return attnPhone;
    }

    public void setAttnPhone(String attnPhone) {
        this.attnPhone = attnPhone;
    }

    public String getAttnPhone2() {
        return attnPhone2;
    }

    public void setAttnPhone2(String attnPhone2) {
        this.attnPhone2 = attnPhone2;
    }

    public String getAttnEmail() {
        return attnEmail;
    }

    public void setAttnEmail(String attnEmail) {
        this.attnEmail = attnEmail;
    }
}
