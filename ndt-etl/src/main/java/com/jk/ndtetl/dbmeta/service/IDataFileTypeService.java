package com.jk.ndtetl.dbmeta.service;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.dbmeta.DataFileType;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IDataFileService 
 * @Description: DataFileService接口
 * @author lianhanwen 
 * @date 2017年6月24日 上午9:09:27 
 *
 */
public interface IDataFileTypeService extends IBaseService<DataFileType> {

    List<Map<String,Object>> listOptionByParam(Map<String,Object> param);
}
