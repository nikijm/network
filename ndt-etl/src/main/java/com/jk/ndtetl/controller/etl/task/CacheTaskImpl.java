package com.jk.ndtetl.controller.etl.task;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.schedule.CacheAutoExecutor;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.exception.TaskException;
import com.jk.ndtetl.schedule.pool.EtlThreadPoolTaskExecutor;
import com.jk.ndtetl.schedule.task.CacheTask;
import com.jk.ndtetl.util.SpringContextHolder;

public class CacheTaskImpl implements TaskRun {

    private final Logger logger = LoggerFactory.getLogger(CacheTaskImpl.class);

    @Resource(name = "cacheThreadPool")
    private EtlThreadPoolTaskExecutor cacheThreadPool;

    @Override
    public boolean supend(String fileId) {
        CacheAutoExecutor cacheAutoExecutor = SpringContextHolder.getBean("cacheAutoExecutor");
        TaskDetailPojo vo = cacheAutoExecutor.getVo(fileId);
        try {
            if (vo == null) {
                rollBack(fileId);
                throw new TaskRejectedException("An instance cannot be found from the fileid.");
            }
            if (vo.isSuspend()) {
                throw new TaskRejectedException("The task is already [suspend] state.");
            }
            vo.setSuspend(true);
            return true;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean stop(String fileId) {
        CacheAutoExecutor cacheAutoExecutor = SpringContextHolder.getBean("cacheAutoExecutor");
        IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
        TaskDetailPojo vo = cacheAutoExecutor.getVo(fileId);
        try {
            if (vo == null) {
                rollBack(fileId);
                throw new TaskRejectedException("An instance cannot be found from the fileid.");
            }
            if (vo.isStop()) {
                throw new TaskRejectedException("The task is already [stop] state.");
            }
            vo.setStop(true);
            if (vo.isSuspend()) {
                DataFile dataFile = dataFileService.getById(Integer.valueOf(fileId));
                cacheThreadPool.execute(new CacheTask(dataFile, vo));
            }
            return true;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            cacheAutoExecutor.removeMap(fileId);
            return false;
        }
    }

    @Override
    public boolean start(String fileId) {
        try {
            IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
            DataFile dataFile = dataFileService.getById(Integer.valueOf(fileId));
            dataFile.setStatusCache(DataFile.DATA_STATUS_READY);
            dataFile.setAction(DataFile.ACTION_READY);
            dataFile.setUpdated(new Date());
            // TODO dataFile.setUpdatedBy(null);
            dataFileService.update(dataFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean reStart(String fileId) {
        CacheAutoExecutor cacheAutoExecutor = SpringContextHolder.getBean("cacheAutoExecutor");
        IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
        TaskDetailPojo vo = cacheAutoExecutor.getVo(fileId);
        try {
            if (vo == null) {
                rollBack(fileId);
                throw new TaskRejectedException("The file is not in the task list.");
            }
            if (!vo.isSuspend()) {
                throw new TaskRejectedException("The task is started.");
            }
            vo.setSuspend(false);
            DataFile dataFile = dataFileService.getById(Integer.valueOf(fileId));
            cacheThreadPool.execute(new CacheTask(dataFile, vo));
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean startTask() {
        try {
            CacheAutoExecutor cacheAutoExecutor = SpringContextHolder.getBean("cacheAutoExecutor");
            cacheAutoExecutor.setCacheOn(true);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean stopTask() {
        try {
            CacheAutoExecutor cacheAutoExecutor = SpringContextHolder.getBean("cacheAutoExecutor");
            cacheAutoExecutor.setCacheOn(false);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取 Vo对象
     * 
     * @param etlDatafileId
     * 文件id
     * @return TaskDetailPojo
     * @throws TaskException
     */
    @Override
    public TaskDetailPojo getVo(Integer etlDatafileId) {
        CacheAutoExecutor cacheAutoExecutor = SpringContextHolder.getBean("cacheAutoExecutor");
        return cacheAutoExecutor.getVo(etlDatafileId == null ? "-1" : etlDatafileId + "");
    }

    /**
     * 回滚缓存数据
     * 
     * @author lianhanwen
     * @date 2017年7月31日 上午9:16:50
     * @param fileId
     * @return
     */
    @Override
    public boolean rollBack(String fileId) {
        IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
        try {
            dataFileService.rollBack(Integer.valueOf(fileId));
            return true;
        }
        catch (CustomException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
