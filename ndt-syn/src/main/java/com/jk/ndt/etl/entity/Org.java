package com.jk.ndt.etl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Org implements Serializable {
    private BigDecimal adOrgId;

    private BigDecimal adOrgtypeId;

    private String logo;

    private String phone;

    private String phone2;

    private String email;

    private String attn;

    private BigDecimal attnId;

    private String attnPhone;

    private String attnPhone2;

    private String attnEmail;

    private String supervisor;

    private BigDecimal supervisorId;

    private String supervisorPhone;

    private String supervisorPhone2;

    private String supervisorEmail;

    private String legal;

    private BigDecimal legalId;

    private String legalPhone;

    private String legalPhone2;

    private String legalEmail;

    private BigDecimal  soCreditlimit;

    private BigDecimal  soCreditused;

    private BigDecimal cLocationId;

    private BigDecimal adAdmindivisionId;

    private Date created;

    private BigDecimal createdby;

    private String description;

    private String isactive;

    private String issummary;

    private BigDecimal isvalid;

    private String name;

    private Date updated;

    private BigDecimal updatedby;

    private String value;

    public BigDecimal getAdOrgId() {
        return adOrgId;
    }

    public void setAdOrgId(BigDecimal adOrgId) {
        this.adOrgId = adOrgId;
    }

    public BigDecimal getAdOrgtypeId() {
        return adOrgtypeId;
    }

    public void setAdOrgtypeId(BigDecimal adOrgtypeId) {
        this.adOrgtypeId = adOrgtypeId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public BigDecimal getAttnId() {
        return attnId;
    }

    public void setAttnId(BigDecimal attnId) {
        this.attnId = attnId;
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

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public BigDecimal getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(BigDecimal supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorPhone() {
        return supervisorPhone;
    }

    public void setSupervisorPhone(String supervisorPhone) {
        this.supervisorPhone = supervisorPhone;
    }

    public String getSupervisorPhone2() {
        return supervisorPhone2;
    }

    public void setSupervisorPhone2(String supervisorPhone2) {
        this.supervisorPhone2 = supervisorPhone2;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public void setSupervisorEmail(String supervisorEmail) {
        this.supervisorEmail = supervisorEmail;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public BigDecimal getLegalId() {
        return legalId;
    }

    public void setLegalId(BigDecimal legalId) {
        this.legalId = legalId;
    }

    public String getLegalPhone() {
        return legalPhone;
    }

    public void setLegalPhone(String legalPhone) {
        this.legalPhone = legalPhone;
    }

    public String getLegalPhone2() {
        return legalPhone2;
    }

    public void setLegalPhone2(String legalPhone2) {
        this.legalPhone2 = legalPhone2;
    }

    public String getLegalEmail() {
        return legalEmail;
    }

    public void setLegalEmail(String legalEmail) {
        this.legalEmail = legalEmail;
    }

    public BigDecimal getSoCreditlimit() {
        return soCreditlimit;
    }

    public void setSoCreditlimit(BigDecimal soCreditlimit) {
        this.soCreditlimit = soCreditlimit;
    }

    public BigDecimal getSoCreditused() {
        return soCreditused;
    }

    public void setSoCreditused(BigDecimal soCreditused) {
        this.soCreditused = soCreditused;
    }

    public BigDecimal getcLocationId() {
        return cLocationId;
    }

    public void setcLocationId(BigDecimal cLocationId) {
        this.cLocationId = cLocationId;
    }

    public BigDecimal getAdAdmindivisionId() {
        return adAdmindivisionId;
    }

    public void setAdAdmindivisionId(BigDecimal adAdmindivisionId) {
        this.adAdmindivisionId = adAdmindivisionId;
    }


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getIssummary() {
        return issummary;
    }

    public void setIssummary(String issummary) {
        this.issummary = issummary;
    }

    public BigDecimal getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(BigDecimal isvalid) {
        this.isvalid = isvalid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCreatedby() {
        return createdby;
    }

    public void setCreatedby(BigDecimal createdby) {
        this.createdby = createdby;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public BigDecimal getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(BigDecimal updatedby) {
        this.updatedby = updatedby;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}