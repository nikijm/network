package com.jk.ndt.etl.service;

import java.util.List;

import com.jk.ndt.etl.entity.Page;

/**
 * 
 * @ClassName: BaseService
 * @Description: 基础接口,包含增删改查
 * @author lianhanwen
 * @date 2017年5月23日 下午3:54:14
 * 
 * @param <T>
 */
public interface BaseService<T> {
    /**
     * 
     * @Description: 插入
     * @author lianhanwen
     * @date 2017年5月23日 下午3:48:14
     * @param t
     */
    void save(T t);

    /**
     * 
     * @Description: 修改
     * @author lianhanwen
     * @date 2017年5月23日 下午3:48:28
     * @param t
     */
    void update(T t);

    /**
     * 
     * @Description: 根据id删除
     * @author lianhanwen
     * @date 2017年5月23日 下午3:48:47
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 
     * @Description:根据id查询
     * @author lianhanwen
     * @date 2017年5月23日 下午3:49:05
     * @param id
     * @return
     */
    T getById(Integer id);

    /**
     * 
     * @Description: 查询所有
     * @author lianhanwen
     * @date 2017年5月23日 下午3:49:20
     * @return
     */
    List<T> listAll();

    /**
     * 
     * @Description: 根据条件返回分页对象
     * @author lianhanwen
     * @date 2017年5月23日 下午3:53:46
     * @param page
     * @return
     */
    List<T> listByPage(Page page);
}