package com.jk.ndtetl.dbmeta.service;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.dbmeta.BusinessType;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: IBusinessTypeService 
 * @author lianhanwen 
 * @date 2017年7月5日 上午10:07:58 
 *
 */
public interface IBusinessTypeService extends IBaseService<BusinessType> {


    List<Map<String, Object>> listOptionByParam(Map<String, Object> param);
}
