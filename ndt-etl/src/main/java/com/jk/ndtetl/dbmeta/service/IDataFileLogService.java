package com.jk.ndtetl.dbmeta.service;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileLog;
import com.jk.ndtetl.system.User;

/**
 * 
 * @ClassName: IDataFileLogService
 * @author lianhanwen
 * @date 2017年7月3日 下午2:51:20
 *
 */
public interface IDataFileLogService extends IBaseService<DataFileLog> {

    /**
     * 记录日志的方法
     * 
     * @author lianhanwen
     * @date 2017年7月11日 上午9:43:29
     * @param user
     * @param totalNum
     * @param dataFile
     */

//    void writeCacheLog(User user, Integer totalNum, DataFile dataFile);


    DataFileLog getErrorLogByFileId(Integer dataFileId);

}
