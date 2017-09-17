package com.jk.ndt.etl.entity.mirror;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @ClassName: MirrorTable
 * @Description: 保存镜像表相关的表名
 * @author fangwei
 * @date 2017年5月31日 上午11:23:54
 *
 */
public class MirrorTable {
    /**
     * id主键
     */
    @JsonIgnore
    private Integer id;
    /**
     * 镜像表名称
     */
    private String name;

    // 过渡字段，表对应的列
    private List<MirrorColumn> columns;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MirrorColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<MirrorColumn> columns) {
        this.columns = columns;
    }

}
