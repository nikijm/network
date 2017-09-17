package com.jk.ndtetl;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.controller.BasePage;

/**
 * @ClassName: BaseServiceImpl
 * @Description: BaseService的实现类
 * @author lianhanwen
 * @date 2017年6月13日 下午6:50:39
 * 
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T> {
    abstract protected BaseDao<T> getDao();

    /**
     * 
     * @Description: 插入
     * @author lianhanwen
     * @date 2017年5月23日 下午3:48:14
     * @param t
     */
    @Override
    public void save(T t) {
        getDao().save(t);
        afterSave();

    }

    /**
     * 
     * @Description: 根据id删除
     * @author lianhanwen
     * @date 2017年5月23日 下午3:48:47
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        getDao().deleteById(id);
        afterDelteById();
    }

    /**
     * 
     * @Description: 修改
     * @author lianhanwen
     * @date 2017年5月23日 下午3:48:28
     * @param t
     */
    @Override
    public void update(T t) {
        getDao().update(t);
        afterUpdte();
    }

    /**
     * 
     * @Description:根据id查询
     * @author lianhanwen
     * @date 2017年5月23日 下午3:49:05
     * @param id
     * @return
     */
    @Override
    public T getById(Integer id) {
        T t = getDao().getById(id);
        afterGetById();
        return t;
    }

    /**
     * 
     * @Description: 查询所有
     * @author lianhanwen
     * @date 2017年5月23日 下午3:49:20
     * @return
     */
    @Override
    public List<T> listAll() {
        List<T> list = getDao().listAll();
        afterListAll();
        return list;
    }

    /**
     * 
     * @Description: 根据条件返回分页对象
     * @author lianhanwen
     * @date 2017年5月23日 下午3:53:46
     * @param page
     * @return
     */
    @Override
    public List<T> listByPage(BasePage page) {
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        List<T> list = getDao().listByPage(page);
        afterListByPage();
        return list;
    }

    @Override
    public List<T> listByParam(Map<String, Object> params) {
        BasePage page = new BasePage();
        page.setParam(params);
        List<T> list = getDao().listByPage(page);
        return list;
    }

    /**
     * 
     * @Description: 删除方法，供子类扩展使用
     * @author fangwei
     * @date 2017年6月13日 下午3:12:49
     */
    protected void afterDelteById() {
    }

    /**
     * 
     * @Description: 保存后置方法，子类扩展使用
     * @author fangwei
     * @date 2017年6月13日 下午3:12:57
     */
    protected void afterSave() {

    }

    /**
     * 
     * @Description: 更新后置方法，供子类扩展使用
     * @author fangwei
     * @date 2017年6月13日 下午3:13:43
     */
    protected void afterUpdte() {

    }

    /**
     * 
     * @Description: 根据id查询对象后置扩展方法
     * @author fangwei
     * @date 2017年6月13日 下午3:15:37
     */
    protected void afterGetById() {

    }

    /**
     * 
     * @Description: 列表查询对象后置扩展方法
     * @author fangwei
     * @date 2017年6月13日 下午3:15:37
     */
    protected void afterListAll() {

    }

    /**
     * 
     * @Description: 分页列表查询对象后置扩展方法
     * @author fangwei
     * @date 2017年6月13日 下午3:15:37
     */
    protected void afterListByPage() {

    }
}
