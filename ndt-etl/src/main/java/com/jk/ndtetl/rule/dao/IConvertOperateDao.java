package com.jk.ndtetl.rule.dao;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.rule.ConvertEntity;

/**
 * @ClassName: IConvertDao
 * @Description: 
 * @author wangzhi
 * @date 2017年7月12日 下午5:08:06
 */
public interface IConvertOperateDao {

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月12日 下午6:14:49
	 * @param tableClean
	 * @return
	 * @return List<String>
	 */
	List<Map<String,Object>> listCleanTableDataByTableName(TableDef tableClean);

	/**
	 * @Description: 
	 * @author wangzhi
	 * @date  2017年7月12日 下午6:23:23
	 * @param convertEntity
	 * @return void
	 */
	void saveConvertData(ConvertEntity convertEntity);
      
}
