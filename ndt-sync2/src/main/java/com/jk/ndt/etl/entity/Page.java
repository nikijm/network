package com.jk.ndt.etl.entity;

import java.util.Map;

/**
 *
 * @ClassName: Page
 * @Description: 对PageHelper类进行扩展，增加查询参数和sql拼装方法
 * @author fangwei
 * @date 2017年5月16日 上午10:33:11
 *
 */

public class Page {

    private int current = 1;// 当前页
    private int page_size = 15;// 每页数目
    private long total;// 总数

    /**
     * 查询参数封装
     */
    private Map<String, Object> param;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

}
