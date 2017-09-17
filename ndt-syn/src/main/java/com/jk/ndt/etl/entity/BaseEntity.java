package com.jk.ndt.etl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 朱生 on 2017/5/20.
 */
public class BaseEntity implements Serializable{

    private String created_user;//创建用户的名称
    private String updated_user;//更新用户的名称
    private Date created_start;//创建起始时间
    private Date created_end;//创建结束时间
    private Date updated_start;//更新起始时间
    private Date updated_end;//更新结束时间

    private Date created_at;//创建时间
    private BigDecimal created_by;//创建者
    private Date updated_at;//更新时间
    private BigDecimal updated_by;//更新者
    private Boolean is_active;//激活状态
    private int is_active_num;//激活状态

    public int getIs_active_num() {
        if (null!=is_active && is_active) {
            return 1;
        }
        return 0;
    }

    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public String getUpdated_user() {
        return updated_user;
    }
    public void setUpdated_user(String updated_user) {
        this.updated_user = updated_user;
    }

    public Date getCreated_start() {
        return created_start;
    }

    public void setCreated_start(Date created_start) {
        this.created_start = created_start;
    }

    public Date getCreated_end() {
        return created_end;
    }

    public void setCreated_end(Date created_end) {
        this.created_end = created_end;
    }

    public Date getUpdated_start() {
        return updated_start;
    }

    public void setUpdated_start(Date updated_start) {
        this.updated_start = updated_start;
    }

    public Date getUpdated_end() {
        return updated_end;
    }

    public void setUpdated_end(Date updated_end) {
        this.updated_end = updated_end;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public BigDecimal getCreated_by() {
        return created_by;
    }

    public void setCreated_by(BigDecimal created_by) {
        this.created_by = created_by;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public BigDecimal getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(BigDecimal updated_by) {
        this.updated_by = updated_by;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }
}
