package com.jk.ndt.etl.rule.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jk.ndt.etl.entity.rule.ParamInput;
import com.jk.ndt.etl.exception.LogicalException;
import com.jk.ndt.etl.rule.StringOperation;

/**
 * 
 * @ClassName: InsertStringOperation
 * @Description: 在制定位置插入特定的字符串
 * @author fangwei
 * @date 2017年5月18日 上午9:31:12
 *
 */
@Component
public class InsertStringOperation implements StringOperation {
    private static final String OPERATION_NAME = "insertStringOperation";
    private static final String POSITION_NAME = "插入位置";
    private static final String INSER_VALUE_NAME = "插入值";
    private static final String PARAM_TYPE = "text";
    private static ThreadLocal<List<ParamInput>> paramInputs = new ThreadLocal<List<ParamInput>>();

    @Override
    public String operate(String field,List<Object> params) throws LogicalException {
        if (params != null && params.size() < 2) {
            throw new LogicalException("插入操作参数数量不正确");
        }
        // 默认第一个参数为查找参数值
        int position = Integer.valueOf(params.get(0).toString());
        // 默认第二个参数作为替换参数值
        String insertValue = String.valueOf(params.get(1));
        List<ParamInput> list = new ArrayList<ParamInput>();
        ParamInput positionParam = new ParamInput();
        positionParam.setName(POSITION_NAME);
        positionParam.setType(PARAM_TYPE);
        positionParam.setValue(String.valueOf(position));
        list.add(positionParam);

        ParamInput insertValueParam = new ParamInput();
        insertValueParam.setName(INSER_VALUE_NAME);
        insertValueParam.setType(PARAM_TYPE);
        insertValueParam.setValue(insertValue);
        list.add(insertValueParam);
        paramInputs.set(list);
        // 使用stringbuilder处理，因为不存在多个线程同时操作一个数据的情况，所以不要担心线程安全问题
        StringBuffer sb = new StringBuffer(field);
        sb.insert(position, insertValue);
        return sb.toString();
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
        //清楚线程池中的变量
        paramInputs.remove();
    }
}
