package com.jk.ndtetl.rule.service;

import com.jk.ndtetl.dbmeta.DataFile;

/**
 * @ClassName: ITransformService
 * @Description: 
 * @author wangzhi
 * @date 2017年7月12日 上午10:24:31
 */
public interface IConvertOperateService {
	/**
	 * 
	 * @Description: 对数据做转换操作
	 * @author wangzhi
	 * @date  2017年7月15日 下午4:55:49
	 * @param dataFile
	 * @return void
	 */
	void convert(DataFile dataFile);

}
