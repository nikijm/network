package com.jk.ndtetl.mon.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.BaseDao;
import com.jk.ndtetl.BaseServiceImpl;
import com.jk.ndtetl.Constant;
import com.jk.ndtetl.mon.LogManager;
import com.jk.ndtetl.mon.dao.LogManagerDao;
import com.jk.ndtetl.mon.service.LogManagerService;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.util.WebUtils;
import com.jk.ndtetl.util.monitor.LogsUtil;
import com.jk.ndtetl.util.promission.LoginSessionUtil;

/**
 * 
 * @ClassName: LogManagerServiceImpl
 * @Description: 日志的service
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Service("/logManagerService")
public class LogManagerServiceImpl extends BaseServiceImpl<LogManager> implements LogManagerService {

    private static final String AUTO_MESSAGE = "自动调度";

    @Autowired
    LogManagerDao logManagerDao;

    @Override
    protected BaseDao<LogManager> getDao() {
        return logManagerDao;
    }

    @Override
    public void info(String message) {
        HttpServletRequest request = WebUtils.getRequest();
        User loginAccount = null;
        if (request != null) {
            loginAccount = LoginSessionUtil.getLoginUser(request);
        }
        LogManager logInfo = LogsUtil.getLogInfo(message, request == null ? AUTO_MESSAGE : request.getRequestURI(),
                loginAccount == null ? AUTO_MESSAGE : loginAccount.getName(), Constant.LOG_LEVEL_INFO);
        logManagerDao.save(logInfo);
    }

    @Override
    public void warn(String message) {
        HttpServletRequest request = WebUtils.getRequest();
        User loginAccount = null;
        if (request != null) {
            loginAccount = LoginSessionUtil.getLoginUser(request);
        }
        LogManager logInfo = LogsUtil.getLogInfo(message, request == null ? AUTO_MESSAGE : request.getRequestURI(),
                loginAccount == null ? AUTO_MESSAGE : loginAccount.getName(), Constant.LOG_LEVEL_WARN);
        logManagerDao.save(logInfo);
    }

    @Override
    public void error(String message) {
        HttpServletRequest request = WebUtils.getRequest();
        User loginAccount = null;
        if (request != null) {
            loginAccount = LoginSessionUtil.getLoginUser(request);
        }
        LogManager logInfo = LogsUtil.getLogInfo(message, request == null ? AUTO_MESSAGE : request.getRequestURI(),
                loginAccount == null ? AUTO_MESSAGE : loginAccount.getName(), Constant.LOG_LEVEL_ERROR);
        logManagerDao.save(logInfo);
    }

    @Override
    public void fatal(String message) {
        HttpServletRequest request = WebUtils.getRequest();
        User loginAccount = null;
        if (request != null) {
            loginAccount = LoginSessionUtil.getLoginUser(request);
        }
        LogManager logInfo = LogsUtil.getLogInfo(message, request == null ? AUTO_MESSAGE : request.getRequestURI(),
                loginAccount == null ? AUTO_MESSAGE : loginAccount.getName(), Constant.LOG_LEVEL_FATAL);
        logManagerDao.save(logInfo);
    }

}
