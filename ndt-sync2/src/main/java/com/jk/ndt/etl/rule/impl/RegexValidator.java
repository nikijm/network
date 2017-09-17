package com.jk.ndt.etl.rule.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.jk.ndt.etl.entity.rule.ParamInput;
import com.jk.ndt.etl.rule.Validator;
import com.jk.ndt.etl.utility.Checker;

/**
 * 
 * @ClassName: RegexValidator
 * @Description: 正则验证器
 * @author fangwei
 * @date 2017年5月18日 上午9:07:12
 *
 */
@Component
public class RegexValidator implements Validator {
    private static final String VALIDATOR_NAME = "regexValidator";
    private static final String NAME = "验证规则";
    private static final String PARAM_TYPE = "text";
    private static ThreadLocal<List<ParamInput>> paramInputs = new ThreadLocal<List<ParamInput>>();

    @Override
    public Boolean validate(String field, List<Object> params) {
        List<ParamInput> list = new ArrayList<ParamInput>();
        boolean flag = true;
        // 没有传递正则表达试，则不需要验证，验证直接通过
        if (Checker.isNullOrEmpty(params)) {
            return true;
        }
        // 返回验证器文本框内容
        for (Object param : params) {
            if (flag) {
                Pattern pattern = Pattern.compile(param.toString());
                Matcher matcher = pattern.matcher(field);
                if (!matcher.matches()) {
                    flag = false;
                }
            }
            ParamInput validateParam = new ParamInput();
            validateParam.setName(NAME);
            validateParam.setType(PARAM_TYPE);
            validateParam.setValue(param.toString());
            list.add(validateParam);
        }
        paramInputs.set(list);
        return flag;
    }

    @Override
    public String getValidatorName() {
        return VALIDATOR_NAME;
    }

    @Override
    public List<ParamInput> getParamInputs() {
        return paramInputs.get();
    }

    @Override
    public void clean() {
        paramInputs.remove();
    }

}
