package com.jk.ndt.etl.rule;

import java.util.ArrayList;
import java.util.List;

import com.jk.ndt.etl.entity.rule.ParamInput;
import com.jk.ndt.etl.entity.rule.ValidatePO;

/**
 * 
 * @ClassName: ValidateUtils
 * @Description: 验证器工具类，验证其内容hard code在代码里面（设计要求，不要问为什么）
 * @author fangwei
 * @date 2017年5月23日 下午2:55:05
 * 
 */
public class ValidateUtils {
    private static List<ValidatePO> validateList = new ArrayList<ValidatePO>();
    static {
        // 默认验证器
        ValidatePO validatePO = new ValidatePO();
        List<ParamInput> paramInputs = new ArrayList<ParamInput>();
        validatePO.setName("不验证");
        validatePO.setValidatorName("defaultValidator");
        validatePO.setParamInputs(paramInputs);
        validateList.add(validatePO);

        // 手机号验证器
        validatePO = new ValidatePO();
        paramInputs = new ArrayList<ParamInput>();
        validatePO.setName("手机");
        validatePO.setValidatorName("regexValidator");
        validatePO.setParamInputs(paramInputs);
        validatePO.setDefaultValue("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");
        validateList.add(validatePO);

        // 身份证
        validatePO = new ValidatePO();
        paramInputs = new ArrayList<ParamInput>();
        validatePO.setName("身份证");
        validatePO.setValidatorName("regexValidator");
        validatePO.setParamInputs(paramInputs);
        validatePO.setDefaultValue("^\\d{15}|^\\d{17}([0-9]|X|x)$");
        validateList.add(validatePO);

        // 正则验证器封装
        validatePO = new ValidatePO();
        paramInputs = new ArrayList<ParamInput>();
        validatePO.setName("正则验证");
        validatePO.setValidatorName("regexValidator");
        ParamInput paramInput = new ParamInput();
        paramInput.setName("验证表达式");
        paramInput.setType("text");
        paramInputs.add(paramInput);
        validatePO.setParamInputs(paramInputs);
        validateList.add(validatePO);
    }

    /**
     * 
     * @Description: 查询验证器model列表
     * @author fangwei
     * @date 2017年5月23日 下午4:18:57
     * @return
     */
    public static List<ValidatePO> getValidateList() {
        return validateList;
    }

    /**
     * 
     * @Description: 根据验证器的名字查询制定验证器modle对象
     * @author fangwei
     * @date 2017年5月23日 下午4:19:11
     * @param name
     * @return
     */
    public static ValidatePO findValidateByName(String name) {
        ValidatePO obj = null;
        for (ValidatePO validatePO : validateList) {
            if (validatePO.getName().equals(name)) {
                obj = validatePO;
                break;
            }
        }
        return obj;
    }

    public static String findValidatorNameByName(String name) {
        ValidatePO obj = findValidateByName(name);
        return obj != null ? obj.getValidatorName() : null;
    }
    
    public static String findDefaultValutByName(String name) {
        ValidatePO obj = findValidateByName(name);
        return obj != null ? obj.getDefaultValue() : null;
    }

}
