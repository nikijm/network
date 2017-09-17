package com.jk.ndt.etl.entity.rule;

import java.util.List;

/**
 * 
 * @ClassName: CleanerColumnPO
 * @Description: 清洗列对象数据封装
 * @author fangwei
 * @date 2017年5月26日 下午2:28:34
 *
 */
public class CleanerColumnPO {
    /**
     * 表的列名
     */
    private String name;

    /**
     * 列对应的清洗器列表
     */
    private List<CleanerPO> operations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CleanerPO> getOperations() {
        return operations;
    }

    public void setOperations(List<CleanerPO> operations) {
        this.operations = operations;
    }

}
