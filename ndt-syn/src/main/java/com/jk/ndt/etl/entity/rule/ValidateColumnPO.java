package com.jk.ndt.etl.entity.rule;

/**
 * 
 * @ClassName: ValidateColumnPO
 * @Description: 类验证对象数据封装
 * @author fangwei
 * @date 2017年5月24日 下午5:00:52
 *
 */
public class ValidateColumnPO {
    /**
     * 表的列名
     */
    private String name;

    /**
     * 列对应的验证器名称
     */
    private ValidatePO validator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValidatePO getValidator() {
        return validator;
    }

    public void setValidator(ValidatePO validator) {
        this.validator = validator;
    }

}
