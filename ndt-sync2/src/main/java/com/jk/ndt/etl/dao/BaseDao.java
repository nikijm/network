package com.jk.ndt.etl.dao;

import java.util.List;

import com.jk.ndt.etl.entity.Page;

/**
 * 
 * @ClassName: BaseDao
 * @Description: 基础的dao,包含基础的增删改查方法,可以继承此dao
 * @author lianhanwen
 * @date 2017年5月23日 下午3:47:36
 * 
 * @param <T>
 */
public interface BaseDao<T> {
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
     * @Description: 根据page分页查询
     * @author lianhanwen
     * @date 2017年5月25日 下午12:26:10
     * @param page
     * @return
     */
    List<T> listByPage(Page page);
}