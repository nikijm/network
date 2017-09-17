package com.jk.ndt.etl.service.mirror.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.dao.mirror.MirrorTableDao;
import com.jk.ndt.etl.entity.mirror.MirrorTable;
import com.jk.ndt.etl.service.BaseServiceImpl;
import com.jk.ndt.etl.service.mirror.MirrorTableService;

/**
 * 
 * @ClassName: MirrorTableServiceImpl
 * @Description: 镜像表相关业务实现
 * @author fangwei
 * @date 2017年5月31日 下午2:01:48
 *
 */
@Service("mirrorTableService")
public class MirrorTableServiceImpl extends BaseServiceImpl<MirrorTable> implements MirrorTableService {
    @Autowired
    private MirrorTableDao mirrorTableDao;

    @Override
    protected BaseDao<MirrorTable> getDao() {
        return mirrorTableDao;
    }

}
