package com.jk.ndtetl.mon.service;

import com.jk.ndtetl.IBaseService;
import com.jk.ndtetl.mon.LogManager;

/**
 * 
 * @ClassName: LogManagerService
 * @Description: 操作日志的接口
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface LogManagerService extends IBaseService<LogManager> {

    public void info(String message);

    public void warn(String message);

    public void error(String message);

    public void fatal(String message);

}
