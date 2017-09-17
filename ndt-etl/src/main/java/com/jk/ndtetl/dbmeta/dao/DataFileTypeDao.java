package com.jk.ndtetl.dbmeta.dao;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.dbmeta.DataFileType;

/**
 * DataFileType的mapper
 * @ClassName: DataFileTypeDao 
 * @author lianhanwen 
 * @date 2017年6月27日 上午9:53:45 
 *
 */
public interface DataFileTypeDao extends BaseDao<DataFileType> {

    List<Map<String,Object>> listOptionByParam(Map<String,Object> param);

}
