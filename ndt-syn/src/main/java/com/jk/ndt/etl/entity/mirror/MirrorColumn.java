package com.jk.ndt.etl.entity.mirror;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: MirrorColumn
 * @Description: 镜像业务表相关列名
 * @author fangwei
 * @date 2017年5月31日 上午11:25:59
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class MirrorColumn {
    /**
     * id主键
     */
    @JsonIgnore
    private Integer id;
    /**
     * 镜像业务表列名
     */
    private String name;
    /**
     * 镜像业务表列名对应的表id
     */
    private Integer tableId;

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

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

}
