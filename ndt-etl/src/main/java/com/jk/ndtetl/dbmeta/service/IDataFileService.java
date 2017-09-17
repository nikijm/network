package com.jk.ndtetl.dbmeta.service;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.util.QueryParam;

/**
 * @ClassName: IDataFileService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lianhanwen
 * @date 2017年6月24日 上午9:09:27
 *
 */
public interface IDataFileService extends IBaseService<DataFile> {

    /**
     * 判断上传文件是否重复
     * 
     * @author lianhanwen
     * @date 2017年6月27日 下午3:49:20
     * @param sha1
     * @return
     */
    Boolean isRepeat(String sha1);

    /**
     * 保存多个文件
     * 
     * @author lianhanwen
     * @date 2017年6月30日 上午10:42:26
     * @param dataFiles
     */
    void saveDatafiles(List<DataFile> dataFiles);

    /**
     * 根据当前登录用户查询当日上传文件个数
     * 
     * @author lianhanwen
     * @date 2017年6月30日 上午10:41:15
     * @param userId
     * @return
     */
    int getFileNumFromTodayByUserId(Integer UserId);

    /**
     * 解析并缓存sheet
     * 
     * @author lianhanwen
     * @date 2017年6月30日 上午11:26:31
     * @param id
     * datafile主键
     * @param user
     * 登录用户
     * @return
     * @throws Exception
     */
    Map<String, Object> analyzeAndCache(DataFile dataFile, User user, String realPath);

    /**
     * @Description:
     * @author wangzhi
     * @date 2017年7月13日 下午12:34:57
     * @param param
     * @return
     * @return List<DataFile>
     */
    List<DataFile> listByCacheStatus(QueryParam queryParam);

    /**
     * @Description:
     * @author wangzhi
     * @date 2017年7月13日 下午2:11:19
     * @param queryParam
     * @return
     * @return List<DataFile>
     */
    List<DataFile> listFileToProcess(QueryParam queryParam);

    /**
     * 缓存时候回滚的方法
     * 
     * @author lianhanwen
     * @date 2017年7月29日 下午5:33:00
     * @param fileId
     * @return
     * @throws CustomException 
     */
    boolean rollBack(Integer fileId) throws CustomException;

}
