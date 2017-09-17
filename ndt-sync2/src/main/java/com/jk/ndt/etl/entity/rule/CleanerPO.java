package com.jk.ndt.etl.entity.rule;

import java.util.List;

import com.jk.ndt.etl.rule.CleanerUtils;

/**
 * 
 * @ClassName: CleanerPO
 * @Description: 清洗器model类封装
 * @author fangwei
 * @date 2017年5月26日 下午1:51:31
 *
 */
public class CleanerPO {
    /**
     * 清洗器的名字
     */
    private String name;
    /**
     * 处理器bean的类名
     */
    private String cleanerName;
    /**
     * 参数输入框对象
     */
    private List<ParamInput> paramInputs;
    /**
     * 输入的参数值，和ParamInput中的value值相同，方便接口数据的传递而已
     */
    private List<Object> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCleanerName() {
        if(this.cleanerName == null && this.name != null){
            this.cleanerName = CleanerUtils.findCleanerNameByName(this.name);
        }
        return cleanerName;
    }

    public void setCleanerName(String cleanerName) {
        this.cleanerName = cleanerName;
    }

    public List<ParamInput> getParamInputs() {
        return paramInputs;
    }

    public void setParamInputs(List<ParamInput> paramInputs) {
        this.paramInputs = paramInputs;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

}
