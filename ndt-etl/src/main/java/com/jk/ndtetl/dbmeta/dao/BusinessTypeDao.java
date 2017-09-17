package com.jk.ndtetl.dbmeta.dao;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.dbmeta.BusinessType;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: BusinessTypeDao 
 * @author lianhanwen 
 * @date 2017年7月5日 上午10:09:48 
 *
 */
public interface BusinessTypeDao extends BaseDao<BusinessType> {

    List<Map<String, Object>> listOptionByParam(Map<String, Object> param);
}
