package com.jk.ndtetl;

import java.util.List;
import java.util.Map;

import com.jk.ndtetl.controller.BasePage;

/**
 * @ClassName: BaseService
 * @Description: 基础接口,包含增删改查
 * @author lianhanwen
 * @date 2017年5月23日 下午3:54:14
 * @param <T>
 */
public interface IBaseService<T> {
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
    List<T> listByPage(BasePage page);
    
    /**
     * 
     * @Description: 根据查询条件查询列表  
     * @author fangwei
     * @date 2017年6月15日 下午4:10:42 
     * @param params
     * @return
     */
    List<T> listByParam(Map<String, Object> params);
}