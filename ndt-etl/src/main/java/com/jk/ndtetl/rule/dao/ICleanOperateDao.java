package com.jk.ndtetl.rule.dao;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.util.QueryParam;

/**
 * @ClassName: ICleanRuleDao
 * @Description: 
 * @author wangzhi
 * @date 2017年7月10日 下午6:01:04
 */
public interface ICleanOperateDao {

	/**
	 * @Description: 通过文件id查询清洗表
	 * @author wangzhi
	 * @date  2017年7月11日 上午9:56:42
	 * @param etlDatafileId
	 * @return
	 * @return List<Map<String,Object>>
	 */
	DataFile  getCleanTableByDataFileId(Integer etlDatafileId);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月11日 上午10:20:28
	 * @param queryParam
	 * @return
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> getCleanDataByParam(QueryParam queryParam);


	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午1:10:00
	 * @param param
	 * @return void
	 */
	void updateCacheCleanStatu(Map<String, Object> param);

	/**
	 * @Description: 回滚校验的数据
	 * @author wangzhi
	 * @date  2017年7月14日 下午2:08:30
	 * @param queryParam2
	 * @return void
	 */
	void updateValidateDataStatu(QueryParam queryParam2);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午2:10:55
	 * @param queryParam
	 * @return
	 * @return Long
	 */
	Long getCountNoValidate(QueryParam queryParam);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午2:23:46
	 * @param queryParam2
	 * @return void
	 */
	void updateCleanValidateStatus(QueryParam queryParam2);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午3:26:11
	 * @param queryParam
	 * @return
	 * @return Long
	 */
	Long getCountNoClean(QueryParam queryParam);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午4:23:25
	 * @param queryParam2
	 * @return void
	 */
	void updateCleanStatus(QueryParam queryParam2);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午4:54:48
	 * @param queryParam2
	 * @return void
	 */
	void updateValidateStatuByTableId(QueryParam queryParam2);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午5:03:18
	 * @param param
	 * @return void
	 */
	void updateCleanDataStatu(Map<String, Object> param);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午5:06:29
	 * @param queryParam2
	 * @return void
	 */
	void updateCleanDataByTableId(QueryParam queryParam2);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午5:14:20
	 * @return
	 * @return long
	 */
	long getCountAll(QueryParam queryParam3);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月14日 下午5:35:19
	 * @param queryParam4
	 * @return
	 * @return long
	 */
	long getCountCleaned(QueryParam queryParam4);

}
