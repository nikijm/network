package com.jk.ndtetl.etl;

import com.jk.ndtetl.BaseEntity;

/**
 * 数据表的基础字段类
 * 
 * @ClassName: BaseDataEntity
 * @author lianhanwen
 * @date 2017年7月10日 下午5:32:31
 *
 */
public class BaseDataEntity extends BaseEntity {

    // 1：自动处理成功 2：人工处理成功 0：待处理 －1：自动处理失败 －2：人工处理失败
    public static final int RESULT_AUTO_SUCCESS = 1;

    public static final int RESULT_MANUAL_SUCCESS = 2;

    public static final int RESULT_PENDING = 0;

    public static final int RESULT_AUTO_FAIL = -1;

    public static final int RESULT_MANUAL_FAIL = -2;

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 文件的uuid
     */
    private String datafile_unid;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 数据在源文件中的行号
     */
    private Integer seqno;

    /**
     * 结果：1：自动处理成功2：人工处理成功 0：待处理 －1：自动处理失败 －2：人工处理失败
     */
    private Integer result;

    /**
     * 结果说明
     */
    private String reason;

    public String getDatafile_unid() {
        return datafile_unid;
    }

    public void setDatafile_unid(String datafile_unid) {
        this.datafile_unid = datafile_unid;
    }

    public Integer getSeqno() {
        return seqno;
    }

    public void setSeqno(Integer seqno) {
        this.seqno = seqno;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
