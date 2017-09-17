package com.jk.ndtetl.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: QueryParam
 * @Description: 
 * @author wangzhi
 * @date 2017年7月10日 下午5:31:01
 */
public class QueryParam {
	
	/**
     * 查询参数封装
     */
    private Map<String, Object> param = new HashMap<String, Object>(4);

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
    
    

}
