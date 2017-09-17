package com.jk.ndtetl.system.service.impl;
/**
 * Created by 朱生 on 2017/6/9.
 */

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.system.dao.CompareDao;
import com.jk.ndtetl.system.service.ICompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 朱生
 * @create 2017-06-09 14:40
 **/
@Service("compareService")
public class ComparseServiceImpl implements ICompareService {

    @Autowired
    private CompareDao compareDao;

    @Override
    public List<Map<String, Object>> listCompareAll(String tableName1, String tableName2,BasePage basePage) {
        PageHelper.startPage(basePage.getCurrent(),basePage.getPage_size(),true);
        return compareDao.listCompareAll(tableName1,tableName2);
    }
}
