package com.jk.ndtetl.mon;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @ClassName: ServerInfoDao
 * @Description: 清理内存的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class MemoryClear implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JSONField(name = "value")
    private Integer useValue; // 内存的使用值

    private Integer percentage; // 内存的使用率

    private String datetime; // 清理的时间

    public Integer getUseValue() {
        return useValue;
    }

    public void setUseValue(Integer useValue) {
        this.useValue = useValue;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}
