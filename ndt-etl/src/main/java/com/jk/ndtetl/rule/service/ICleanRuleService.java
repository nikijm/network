package com.jk.ndtetl.rule.service;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.util.QueryParam;

/**
 * 
 * @ClassName: ICleanRuleService
 * @Description: 清洗接口类
 * @author 王志
 * @date 2017年6月23日 下午2:46:36
 *
 */

public interface ICleanRuleService {
    /**
     * 
     * @Description: 清洗数据
     * @author 王志
     * @date 2017年6月23日 下午2:46:07
     * @param field
     * @param params
     * @return
     */
	Boolean clean(DataFile dataFile)throws Exception;
    /**
     * 
     * @Description: 验证
     * @author wangzhi
     * @date  2017年7月11日 下午5:44:26
     * @param dataFile
     * @return
     * @return Boolean
     */
    public boolean validate(DataFile dataFile)throws Exception;
	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午2:09:43
	 * @param queryParam
	 * @return
	 * @return Long
	 */
	Long getCountNoValidate(QueryParam queryParam);
	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午2:23:06
	 * @param queryParam2
	 * @return void
	 */
	void updateCleanValidateStatus(QueryParam queryParam2);
	
	/**
	 * @Description: 修改清洗的参数
	 * @author wangzhi
	 * @date  2017年7月14日 下午2:23:06
	 * @param queryParam2
	 * @return void
	 */
	void updateCleanStatus(QueryParam queryParam2);
	/**
	 * @Description: 查询没有清理的数量
	 * @author wangzhi
	 * @date  2017年7月14日 下午3:23:42
	 * @param queryParam
	 * @return
	 * @return Long
	 */
	Long getCountNoClean(QueryParam queryParam);
	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午4:53:16
	 * @param queryParam2
	 * @return void
	 */
	void updateValidateStatuByTableId(QueryParam queryParam2);
	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午5:05:52
	 * @param queryParam2
	 * @return void
	 */
	void updateCleanDataByTableId(QueryParam queryParam2);
	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午5:13:02
	 * @return void
	 */
	long getCountAll(QueryParam queryParam3);
	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午5:32:32
	 * @return
	 * @return long
	 */
	long getCountCleaned(QueryParam queryParam4);
	
}
