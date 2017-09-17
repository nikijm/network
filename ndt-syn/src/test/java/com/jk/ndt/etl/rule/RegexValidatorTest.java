package com.jk.ndt.etl.rule;

import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.utility.SpringContextHolder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RegexValidatorTest extends SpringTest{
    @Test
    public void validateTest(){
//        String taskString = "18782978148";
//        String validatorName = "regexValidator";
//        List<Object> params = new ArrayList<Object>();
//        //手机号验证
//        params.add("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");
//        Object bean = SpringContextHolder.getBean(validatorName);
//        Validator validator = (Validator)bean;
//        boolean result = validator.validate(taskString, params);
//        Assert.assertTrue(result);
//        System.out.println("验证成功："+result);
//        System.out.println(validator.getParamInputs());
//        //错误的手机号
//        taskString = "10782978148";
//        result = validator.validate(taskString, params);
//        Assert.assertFalse(result);
//        System.out.println("验证成功："+result);
//        System.out.println(validator.getParamInputs());
    }
}
