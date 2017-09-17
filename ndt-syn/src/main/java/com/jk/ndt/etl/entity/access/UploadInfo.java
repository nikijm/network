package com.jk.ndt.etl.entity.access;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jk.ndt.etl.entity.system.Admin;

/**
 * 
 * @ClassName: UploadInfo
 * @Description: ETL_UPLOADS表对应的实体类
 * @author lianhanwen
 * @date 2017年5月17日 下午10:29:07
 *
 */
// @JsonInclude(value = Include.NON_NULL)
public class UploadInfo {
    /**
     * 唯一ID
     */
    private Integer id;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件保存的路径
     */
    private String path;
    /**
     * 数据来源
     */
    private String source;
    /**
     * 用户ID
     */
    private Admin user;
    /**
     * 状态,-1:删除,0:未缓存,1:已缓存,2:处理中,3:清洗完毕,4:全部处理完毕;
     */
    private Object status;

    /**
     * 上传文件中的sheet的数目
     */
    private Integer sheetsNum;
    /**
     * 上传时间
     */
    private Date uploadDate;
    /**
     * SHA1码,用于检查文件是否重复
     */
    private String sha1;
    /**
     * 文件类型
     */
    private String type;

    /**
     * 多对一关系
     */
    // private List<SheetInfo> sheets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Admin getUser() {
        return user;
    }

    public void setUser(Admin user) {
        this.user = user;
    }

    public Integer getSheetsNum() {
        return sheetsNum;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public void setSheetsNum(Integer sheetsNum) {
        this.sheetsNum = sheetsNum;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getUploadDate() {
        return uploadDate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    // public List<SheetInfo> getSheets() {
    // return sheets;
    // }
    //
    // public void setSheets(List<SheetInfo> sheets) {
    // this.sheets = sheets;
    // }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UploadInfo [fileName=" + fileName + ", path=" + path + ", source=" + source + ", user=" + user
                + ", status=" + status + ", sheetsNum=" + sheetsNum + ", uploadDate=" + uploadDate + ", sha1=" + sha1
                + ", type=" + type + "]";
    }

}
