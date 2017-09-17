package com.jk.ndtetl.dbmeta;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.jk.ndtetl.BaseEntity;
import com.jk.ndtetl.system.User;

/**
 * @ClassName: DataFile
 * @Description: ETL_DATAFILE的实体类
 * @author lianhanwen
 * @date 2017年6月22日 下午3:10:01
 *
 */
public class DataFile extends BaseEntity {

    /**
     * 上传文件的地址
     */
    public static final String UPLOAD_URL = "upload";

    /**
     * 失败
     */
    public static final int DATA_STATUS_FAILED = -2;

    /**
     * 未开始
     */
    public static final int DATA_STATUS_NOTSTART = -1;

    /**
     * 处理中
     */
    public static final int DATA_STATUS_STARTED = 0;

    /**
     * 已完成
     */
    public static final int DATA_STATUS_FINISHED = 1;

    /**
     * 准备
     */
    public static final int DATA_STATUS_READY = 2;

    /**
     * 暂停
     */
    public static final int DATA_STATUS_PAUSE = 3;

    public static final String ACTION_CL_VALIDAT_DATA = "清除验证表的数据";

    public static final String ACTION_CL_CONVERT_DATA = " 清除转换表的数据";

    public static final String ACTION_CL_CLEAN_DATA = "清除清洗的数据";

    public static final String ACTION_CL_CACHE_DATA = "清除缓存的数据";

    public static final String ACTION_READY = "添加到调度队列";

    public static final String ACTION_CACHE = "缓存入库";

    public static final String ACTION_CLEAN = "清洗";

    public static final String ACTION_CONVERT = "转换";

    public static final String ACTION_VALIDAT = "验证";

    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1L;

    /**
     * 上传的文件
     * 
     * @return
     */
    private MultipartFile file;

    /**
     * 上传文件的id,序列递增
     */
    private Integer etlDatafileId;

    /**
     * 上传文件类型的id,序列递增
     */
    @NotNull(message = "请选择文件类型")
    private Integer dataFileTypeId;

    /**
     * 文件类型id
     */
    private DataFileType dataFileType;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件保存服务器的路径
     */
    private String path;

    /**
     * 上传单位编码
     */
    private String sourceOrgCode;

    /**
     * 上传人
     */
    private User uploadUser;

    /**
     * 唯一编号uuid
     */
    private String unId = UUID.randomUUID().toString();

    /**
     * 上传状态
     */
    private Integer statusUpload;

    /**
     * 缓存状态
     */
    private Integer statusCache;

    /**
     * 清洗状态
     */
    private Integer statusClean;

    /**
     * 转换状态
     */
    private Integer statusConvert;

    /**
     * 验证状态
     */
    private Integer statusValidate;

    /**
     * 操作
     */
    private String action;

    /**
     * 状态当前处理状态 CO:完成,CL:结束,TN:终止,PS:暂停,IP:处理中 ,EX:异常
     */
    private String status;

    /**
     * 处理说明
     */
    private String message;

    /**
     * 上传时间
     */
    private Date uploadDate;

    /**
     * 文件的sha1码
     */
    private String sha1;

    /**
     * sheet的序号
     */
    private Integer sheetIndex;

    /**
     * 待解析文件的开始行数
     */
    private Integer sheetStartRow = 0;

    /**
     * 文件的表头(排序后)
     */
    private String header;

    // 机构id
    private Integer orgId;

    // 机构名
    private String orgName;

    // 文件夹链接
    private String folderLink;

    // 文件链接
    private String fileLink;

    // 上传类型 0 文件上传 1 文件夹链接 2 文件链接
    private String uploadType;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getFolderLink() {
        return folderLink;
    }

    public void setFolderLink(String folderLink) {
        this.folderLink = folderLink;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getEtlDatafileId() {
        return etlDatafileId;
    }

    public void setEtlDatafileId(Integer etlDatafileId) {
        this.etlDatafileId = etlDatafileId;
    }

    public DataFileType getDataFileType() {
        return dataFileType;
    }

    public void setDataFileType(DataFileType dataFileType) {
        this.dataFileType = dataFileType;
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

    public String getSourceOrgCode() {
        return sourceOrgCode;
    }

    public void setSourceOrgCode(String sourceOrgCode) {
        this.sourceOrgCode = sourceOrgCode;
    }

    public String getUnId() {
        return unId;
    }

    public void setUnId(String unId) {
        this.unId = unId;
    }

    public User getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(User uploadUser) {
        this.uploadUser = uploadUser;
    }

    public Integer getStatusUpload() {
        return statusUpload;
    }

    public void setStatusUpload(Integer statusUpload) {
        this.statusUpload = statusUpload;
    }

    public Integer getStatusCache() {
        return statusCache;
    }

    public void setStatusCache(Integer statusCache) {
        this.statusCache = statusCache;
    }

    public Integer getStatusClean() {
        return statusClean;
    }

    public void setStatusClean(Integer statusClean) {
        this.statusClean = statusClean;
    }

    public Integer getStatusConvert() {
        return statusConvert;
    }

    public void setStatusConvert(Integer statusConvert) {
        this.statusConvert = statusConvert;
    }

    public Integer getStatusValidate() {
        return statusValidate;
    }

    public void setStatusValidate(Integer statusValidate) {
        this.statusValidate = statusValidate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Integer getSheetStartRow() {
        return sheetStartRow;
    }

    public void setSheetStartRow(Integer sheetStartRow) {
        this.sheetStartRow = sheetStartRow;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Integer getDataFileTypeId() {
        return dataFileTypeId;
    }

    public void setDataFileTypeId(Integer dataFileTypeId) {
        this.dataFileTypeId = dataFileTypeId;
    }

}