package com.jk.ndt.etl.entity.rule;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jk.ndt.etl.rule.ValidateUtils;
import com.jk.ndt.etl.utility.Checker;

/**
 * 
 * @ClassName: ValidatePO
 * @Description: 验证器model类，用于封装验证器常见熟悉
 * @author fangwei
 * @date 2017年5月23日 下午4:11:42
 *
 */
public class ValidatePO {
    /**
     * 验证器的名字
     */
    private String name;
    /**
     * 处理器bean的类名
     */
    private String validatorName;
    /**
     * 参数输入框对象
     */
    private List<ParamInput> paramInputs;
    /**
     * 输入的参数值，和ParamInput中的value值相同，方便接口数据的传递而已
     */
    private List<Object> params;
    /**
     * 默认验证正则表达式
     */
    @JsonIgnore
    private String defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParamInput> getParamInputs() {
        return paramInputs;
    }

    public void setParamInputs(List<ParamInput> paramInputs) {
        this.paramInputs = paramInputs;
    }

    public List<Object> getParams() {
        return this.params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public String getValidatorName() {
        return validatorName;
    }

    public void setValidatorName(String validatorName) {
        this.validatorName = validatorName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
