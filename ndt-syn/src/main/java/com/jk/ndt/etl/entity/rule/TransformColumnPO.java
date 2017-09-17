package com.jk.ndt.etl.entity.rule;

/**
 * 
 * @ClassName: TransformColumnPO
 * @Description: 转换器列模型类
 * @author fangwei
 * @date 2017年5月31日 下午4:43:07
 *
 */
public class TransformColumnPO {
    /**
     * 转换的列名称
     */
    private String name;
    /**
     * 转换前来的中文列名
     */
    private String from;
    /**
     * 真实数据库中对应的转换前的列名
     */
    private String fromColumnName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromColumnName() {
        return fromColumnName;
    }

    public void setFromColumnName(String fromColumnName) {
        this.fromColumnName = fromColumnName;
    }

}
