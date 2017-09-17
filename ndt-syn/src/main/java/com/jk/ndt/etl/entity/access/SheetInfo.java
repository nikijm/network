package com.jk.ndt.etl.entity.access;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: SheetInfo
 * @Description: ETL_SHEETS表的实体类
 * @author lianhanwen
 * @date 2017年5月17日 下午10:28:22
 *
 */
//@JsonInclude(value = Include.NON_NULL)
public class SheetInfo {
    /**
     * 唯一ID,序列递增
     */
    private Integer id;
    /**
     * 上传时候的ID
     */
    private UploadInfo upload;
    /**
     * 状态,-1:未验证,0:已验证,1:已清洗,2:已转换
     */
    private Object status;
    /**
     * 原始表头
     */
    private String columns;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UploadInfo getUpload() {
        return upload;
    }

    public void setUpload(UploadInfo upload) {
        this.upload = upload;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "SheetInfo [id=" + id + ", upload=" + upload + ", status=" + status + ", columns=" + columns + "]";
    }

}
