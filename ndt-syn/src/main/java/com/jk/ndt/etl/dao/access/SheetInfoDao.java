package com.jk.ndt.etl.dao.access;

import java.util.List;

import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.entity.access.SheetInfo;

/**
 * 
 * @ClassName: SheetInfoMapper
 * @Description: sheet可能需要的操作方法
 * @author lianhanwen
 * @date 2017年5月17日 下午10:13:21
 *
 */
public interface SheetInfoDao extends BaseDao<SheetInfo> {

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
