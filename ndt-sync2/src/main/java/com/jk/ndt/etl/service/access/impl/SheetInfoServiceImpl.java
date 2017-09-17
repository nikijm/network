package com.jk.ndt.etl.service.access.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.dao.access.SheetInfoDao;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.service.BaseServiceImpl;
import com.jk.ndt.etl.service.access.SheetInfoService;

@Service("sheetInfoService")
public class SheetInfoServiceImpl extends BaseServiceImpl<SheetInfo> implements SheetInfoService {
    @Autowired
    private SheetInfoDao sheetInfoDao;

    @Override
    protected BaseDao<SheetInfo> getDao() {
        return sheetInfoDao;
    }

    @Override
    public void deleteByUploadId(Integer uploadId) {
        sheetInfoDao.deleteByUploadId(uploadId);
    }

    @Override
    public List<SheetInfo> getByUploadId(Integer uploadId) {
        return  sheetInfoDao.getByUploadId(uploadId);
    }

}
