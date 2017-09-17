package com.jk.ndt.etl.entity.rule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 
 * @ClassName: ParamInput
 * @Description: 对页面输入值的参数框进行封装
 * @author fangwei
 * @date 2017年5月18日 上午10:49:24
 *
 */
public class ParamInput {
    @JsonIgnore
    private Integer id;
    /**
     * 用于在输入框之前显示，即checkbox的label
     */
    private String name;
    /**
     * 预留小工具字段
     */
    @JsonIgnore
    private String tooltip;
    /**
     * 输入框的类型，可以是checkbox, select, text
     */
    private String type;
    /**
     * 只针对type为select的字段有效
     */
    private List<String> options;
    /**
     * 用户录入的值
     */
    private String value;

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

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ParamInput [id=" + id + ", name=" + name + ", tooltip=" + tooltip + ", type=" + type + ", options="
                + options + ", value=" + value + "]";
    }
    
}
