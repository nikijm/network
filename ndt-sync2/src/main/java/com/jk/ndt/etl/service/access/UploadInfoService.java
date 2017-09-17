package com.jk.ndt.etl.service.access;

import java.util.List;

import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.entity.access.UploadInfo;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.service.BaseService;

/**
 * 
 * @ClassName: UploadInfoService
 * @Description: 上传时可能需要的操作方法
 * @author lianhanwen
 * @date 2017年5月18日 上午11:25:47
 *
 */
public interface UploadInfoService extends BaseService<UploadInfo>{

    /**
     * @Description: 验证文件是否已经存在
     * @author lianhanwen
     * @date 2017年5月19日 上午10:52:13
     * @param sha1
     * @return
     */
    Boolean isRepeat(String sha1);


    void cache(Integer id, Admin user, List<DataTable> list) throws Exception;

}
