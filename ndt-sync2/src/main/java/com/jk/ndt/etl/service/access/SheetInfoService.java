package com.jk.ndt.etl.service.access;

import java.util.List;

import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.service.BaseService;

/**
 * @ClassName: SheetInfoService
 * @Description: 上传时可能需要的操作方法
 * @author lianhanwen
 * @date 2017年5月18日 上午11:24:54
 *
 */

public interface SheetInfoService extends BaseService<SheetInfo> {

    /**
     * 
     * @Description: 根据upload_id删除
     * @author lianhanwen
     * @date 2017年5月22日 下午5:07:50
     * @param uploadId
     */
    void deleteByUploadId(Integer uploadId);

    /**
     * 
     * @Description: 根据upload_id查询
     * @author lianhanwen
     * @date 2017年5月26日 下午5:50:37
     * @param uploadId
     * @return
     */
    List<SheetInfo> getByUploadId(Integer uploadId);
}
