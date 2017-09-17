package com.jk.ndt.etl.dao.access;

import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.entity.access.UploadInfo;

/**
 * 
 * @ClassName: UploadInfoMapper
 * @Description: 上传时可能需要的操作方法
 * @author lianhanwen
 * @date 2017年5月17日 下午10:13:21
 *
 */
public interface UploadInfoDao extends BaseDao<UploadInfo>{

    /**
     * @Description: 根据sha1码查询
     * @author lianhanwen
     * @date 2017年5月19日 上午10:54:36
     * @param sha1
     * @return
     */
    UploadInfo getBySha1(String sha1);

}
