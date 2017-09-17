package com.jk.ndt.etl.service;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.jk.ndt.etl.dao.BaseDao;
import com.jk.ndt.etl.entity.Page;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    abstract protected BaseDao<T> getDao();

    @Override
    public void save(T t) {
        getDao().save(t);

    }

    @Override
    public void deleteById(Integer id) {
        getDao().deleteById(id);
    }

    @Override
    public void update(T t) {
        getDao().update(t);
    }

    @Override
    public T getById(Integer id) {
        return getDao().getById(id);
    }

    @Override
    public List<T> listAll() {
        return getDao().listAll();
    }

    @Override
    public List<T> listByPage(Page page) {
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        return getDao().listByPage(page);
    }

}
