package com.jk.ndtetl.dbmeta.dao;

import java.util.List;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.dbmeta.DataFileLog;

/**
 * 数据日志的dao
 * 
 * @ClassName: DataFileDaoLog
 * @author lianhanwen
 * @date 2017年7月3日 下午2:18:18
 *
 */
public interface DataFileLogDao extends BaseDao<DataFileLog> {

    /**
     * 根据文件id查询日志
     * 
     * @author lianhanwen
     * @date 2017年7月24日 下午2:33:40
     * @param dataFileId
     * @return
     */
    List<DataFileLog> getByFileId(Integer dataFileId);

    /**
     * 获取一个文件总共的unknowNum
     * 
     * @author lianhanwen
     * @date 2017年7月27日 下午6:13:24
     * @param dataFileId
     * @return
     */
    int getUnKnowNumByFileId(Integer dataFileId);
}
