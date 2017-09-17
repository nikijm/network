package com.jk.ndt.etl.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: Paging 
 * @Description: 用于转换json给前台的paging对象 
 * @author lianhanwen 
 * @date 2017年5月25日 下午6:21:43 
 *
 */
//@JsonInclude(value = Include.NON_NULL)
public class Paging {
    private Integer current;// 当前页
    private Integer page_size;// 每页数目
    private Long total;// 每页数目
    public Integer getCurrent() {
        return current;
    }
    public void setCurrent(Integer current) {
        this.current = current;
    }
    public Integer getPage_size() {
        return page_size;
    }
    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
  
    
}
