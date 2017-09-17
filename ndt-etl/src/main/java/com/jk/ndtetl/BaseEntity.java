package com.jk.ndtetl;

import java.io.Serializable;
import java.util.Date;

/**
 * 朱生
 * 
 * @create 2017-06-23 9:01
 **/
public class BaseEntity implements Serializable {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 创建者
     */
    private Integer createdBy;
    /**
     * 修改时间
     */
    private Date updated;
    /**
     * 修改者
     */
    private Integer updatedBy;
    /**
     * 激活状态 Y 激活 N
     */
    private Character isActive;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Character getIsActive() {
        return isActive;
    }

    public void setIsActive(Character isActive) {
        this.isActive = isActive;
    }

}
