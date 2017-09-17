package com.jk.ndtetl;

import java.util.Date;

/**
 * Created by 朱生 on 2017/5/20.
 */
public class BaseSystemEntity extends BaseEntity  {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 分页常量
     */
    public static final String BASE_PAGE = "paging";

    private String createdUser;//创建用户的名称
    private String updatedUser;//更新用户的名称
    private Date createdStart;//创建起始时间
    private Date createdEnd;//创建结束时间
    private Date updatedStart;//更新起始时间
    private Date updatedEnd;//更新结束时间
    private String searchKey;//搜索内容

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Date getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(Date createdStart) {
        this.createdStart = createdStart;
    }

    public Date getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(Date createdEnd) {
        this.createdEnd = createdEnd;
    }

    public Date getUpdatedStart() {
        return updatedStart;
    }

    public void setUpdatedStart(Date updatedStart) {
        this.updatedStart = updatedStart;
    }

    public Date getUpdatedEnd() {
        return updatedEnd;
    }

    public void setUpdatedEnd(Date updatedEnd) {
        this.updatedEnd = updatedEnd;
    }
}
