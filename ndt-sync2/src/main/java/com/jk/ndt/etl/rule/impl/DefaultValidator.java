package com.jk.ndt.etl.rule.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jk.ndt.etl.entity.rule.ParamInput;
import com.jk.ndt.etl.rule.Validator;

/**
 * 
 * @ClassName: DefaultValidator 
 * @Description: 默认验证器，不做任何验证处理
 * @author fangwei 
 * @date 2017年5月23日 下午4:02:19 
 *
 */
@Component
public class DefaultValidator implements Validator{

    private static final String VALIDATOR_NAME = "defaultValidator";
    @Override
    public Boolean validate(String field, List<Object> params) {
        return true;
    }

    @Override
    public String getValidatorName() {
        return VALIDATOR_NAME;
    }

    @Override
    public List<ParamInput> getParamInputs() {
        return new ArrayList<ParamInput>();
    }

    @Override
    public void clean() {
        // TODO Auto-generated method stub
    }

}
