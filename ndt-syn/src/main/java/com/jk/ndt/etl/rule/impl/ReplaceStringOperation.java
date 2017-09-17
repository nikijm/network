package com.jk.ndt.etl.rule.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jk.ndt.etl.entity.rule.ParamInput;
import com.jk.ndt.etl.exception.LogicalException;
import com.jk.ndt.etl.rule.StringOperation;
import com.jk.ndt.etl.utility.StringUtils;

/**
 * 
 * @ClassName: ReplaceStringOperation
 * @Description: 替换字符串操作
 * @author fangwei
 * @date 2017年5月18日 上午9:20:37
 *
 */
@Component
public class ReplaceStringOperation implements StringOperation {
    private static final String OPERATION_NAME = "replaceStringOperation";
    private static final String SEARCH_NAME = "查找值";
    private static final String REPLACE_NAME = "替换值";
    private static final String PARAM_TYPE = "text";
    private static ThreadLocal<List<ParamInput>> paramInputs = new ThreadLocal<List<ParamInput>>();

    @Override
    public String operate(String field, List<Object> params) throws LogicalException {
        if (params != null && params.size() < 2) {
            throw new LogicalException("替换操作参数数量不正确");
        }
        // 默认第一个参数为查找参数值
        String regex = String.valueOf(params.get(0));
        // 默认第二个参数作为替换参数值
        String replacement = String.valueOf(params.get(1));
        List<ParamInput> list = new ArrayList<ParamInput>();
        ParamInput searchParam = new ParamInput();
        searchParam.setName(SEARCH_NAME);
        searchParam.setType(PARAM_TYPE);
        searchParam.setValue(regex);
        list.add(searchParam);

        ParamInput replaceParam = new ParamInput();
        replaceParam.setName(REPLACE_NAME);
        replaceParam.setType(PARAM_TYPE);
        replaceParam.setValue(replacement);
        list.add(replaceParam);
        paramInputs.set(list);

        String newString = field.replaceAll(StringUtils.escapeExprSpecialWord(regex), replacement);
        return newString;
    }

    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

    @Override
    public List<ParamInput> getParamInputs() {
        List<ParamInput> list = paramInputs.get();
        return list;
    }

    @Override
    public void clean() {
        //清除线程变量中的值
        paramInputs.remove();
    }

}
