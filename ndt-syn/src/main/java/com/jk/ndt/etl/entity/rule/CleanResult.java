package com.jk.ndt.etl.entity.rule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @ClassName: CleanResult
 * @Description: 清洗结果类
 * @author fangwei
 * @date 2017年6月2日 上午9:17:18
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class CleanResult {
    /**
     * 清洗是否成功
     */
    private Boolean isCleaned;
    /**
     * 清洗相关提示信息，错误的时候有值
     */
    private String message;

    public Boolean getIsCleaned() {
        return isCleaned;
    }

    public void setIsCleaned(Boolean isCleaned) {
        this.isCleaned = isCleaned;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
