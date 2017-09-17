package com.jk.ndtetl.dbmeta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.schedule.exception.DataException;
import com.jk.ndtetl.util.QueryParam;

/**
 * @ClassName: DataFileDao
 * @Description: DataFileDao
 * @author lianhanwen
 * @date 2017年6月23日 下午4:58:49
 *
 */
public interface DataFileDao extends BaseDao<DataFile> {

    /**
     * 根据sha1查询
     * 
     * @author lianhanwen
     * @date 2017年6月27日 下午3:39:02
     * @param sha1
     * @return
     */
    List<DataFile> getBySha1(String sha1);

    /**
     * 根据id查询
     * 
     * @author lianhanwen
     * @date 2017年7月24日 下午2:13:54
     * @param dataFileId
     * @return
     */
    DataFile getTableById(Integer dataFileId);

    /**
     * 根据当前登录用户查询当日上传文件个数
     * 
     * @author lianhanwen
     * @date 2017年6月30日 上午10:41:15
     * @param userId
     * @return
     */
    int getFileNumFromTodayByUserId(Integer userId);

    /**
     * @Description:
     * @author wangzhi
     * @date 2017年7月13日 下午12:36:31
     * @param param
     * @return
     * @return List<DataFile>
     */
    List<DataFile> listByCacheStatus(QueryParam queryParam);

    /**
     * @Description:
     * @author wangzhi
     * @date 2017年7月13日 下午2:12:31
     * @param queryParam
     * @return
     * @return List<DataFile>
     */
    List<DataFile> listFileToProcess(QueryParam queryParam);
    
    DataFile getDataFile(@Param("fileId")Integer id) throws DataException;
}
