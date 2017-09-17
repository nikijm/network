package com.jk.ndt.etl.rule;

import java.util.List;

import com.jk.ndt.etl.entity.rule.ParamInput;

/**
 * 
 * @ClassName: Validator 
 * @Description: 验证器接口，提供字符串验证方法 
 * @author fangwei 
 * @date 2017年5月17日 下午5:52:18 
 *
 */
public interface Validator {
    /**
     * 
     * @Description: 验证字符串信息  
     * @author fangwei
     * @date 2017年5月17日 下午5:53:38 
     * @param field
     * @param params
     * @return
     */
    Boolean validate(String field,List<Object> params);
    
    /**
     * 
     * @Description: 获取验证器的名称  
     * @author fangwei
     * @date 2017年5月20日 下午1:31:44 
     * @return
     */
    String getValidatorName();
    
    /**
     * 
     * @Description: 获取输入参数对象，该对象在validate操纵后才有值  
     * @author fangwei
     * @date 2017年5月20日 下午3:21:45 
     * @return
     */
    List<ParamInput> getParamInputs();
    
    /**
     * 
     * @Description: 清理相关变量，避免变量污染  
     * @author fangwei
     * @date 2017年5月22日 上午8:57:21
     */
    void clean();
}
