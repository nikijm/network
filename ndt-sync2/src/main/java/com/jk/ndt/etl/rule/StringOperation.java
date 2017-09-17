package com.jk.ndt.etl.rule;

import java.util.List;

import com.jk.ndt.etl.entity.rule.ParamInput;
import com.jk.ndt.etl.exception.LogicalException;

import javafx.fxml.LoadException;

/**
 * 
 * @ClassName: StringOperation
 * @Description: 字符串操作接口
 * @author fangwei
 * @date 2017年5月18日 上午8:39:25
 *
 */
public interface StringOperation {
    /**
     * 
     * @Description: 将字符串按照规则进行处理
     * @author fangwei
     * @date 2017年5月18日 上午8:40:27
     * @param field
     * @param params
     * @return
     * @throws LoadException 
     */
    String operate(String field,List<Object> params) throws LogicalException;
    /**
     * 
     * @Description: 获取处理器的名称  
     * @author fangwei
     * @date 2017年5月20日 下午1:31:44 
     * @return
     */
    String getOperationName();
    
    /**
     * 
     * @Description: 获取输入参数对象，该对象在operate操纵后才有值  
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
