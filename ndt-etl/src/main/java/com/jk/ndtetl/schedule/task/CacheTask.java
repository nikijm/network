package com.jk.ndtetl.schedule.task;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileLog;
import com.jk.ndtetl.dbmeta.service.IDataFileLogService;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.schedule.AutoExecutor;
import com.jk.ndtetl.schedule.CacheAutoExecutor;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.exception.TaskException;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.SpringContextHolder;

/**
 * 
 * @ClassName: CacheTask
 * @Description: 缓存任务逻辑
 * @author fangwei
 * @date 2017年6月15日 上午10:52:56
 *
 */
public class CacheTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CacheTask.class);

    private DataFile dataFile;

    private TaskDetailPojo taskDetailPojo;

    public CacheTask(DataFile dataFile, TaskDetailPojo taskDetailPojo) {
        super();
        this.dataFile = dataFile;
        this.taskDetailPojo = taskDetailPojo;
    }

    @Resource(name = "autoExecutor")
    private AutoExecutor autoExecutor;

    public void run() {
        IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
        CacheAutoExecutor cacheAutoExecutor = SpringContextHolder.getBean("cacheAutoExecutor");
        
        User loginUser = taskDetailPojo.getUser();
        String realPath = Thread.currentThread().getContextClassLoader().getResource("/").getPath()
                .replace("/WEB-INF/classes/", "/");
        
        try {
            if (taskDetailPojo.isSuspend() && taskDetailPojo.isStop()) {
                boolean isRollBack = dataFileService.rollBack(dataFile.getEtlDatafileId());
                if (!isRollBack) {
                    writeCacheLog(loginUser, null, "回滚失败", dataFile);
                    throw new TaskException("ETL0024", "fileId=" + dataFile.getEtlDatafileId() + "rollback failed.");
                }
            }

            /**
             * 1.根据文件ID修改状态为running，修改成功则继续，修改失败抛出异常或者return
             * 2.根据文件ID去服务器去上查找文件，并确认文件存在，文件存在开始解析，不存在，将running状态改成failed.并记录原因，
             * 线程结束 3.按照配置条数开始解析文件，解析成功，则将入库进度实时刷新到vo中
             * 解析失败则将running状态改成failed.并记录原因，然后将入库的数据一一回滚线程结束
             * 4.解析完成并且没有报错在日志中记录解析成功的详情
             */

            dataFile.setStatusCache(DataFile.DATA_STATUS_STARTED);
            dataFileService.update(dataFile);
            Integer etlDatafileId = dataFile.getEtlDatafileId();
            while (true) {

                if (taskDetailPojo.isSuspend()) {
                    logger.info("fileId=" + etlDatafileId + " were suspended.");
                    return;
                }

                if (taskDetailPojo.isStop()) {
                    logger.info("fileId=" + etlDatafileId + " were stoped.");
                    boolean isRollBack = dataFileService.rollBack(etlDatafileId);
                    if (!isRollBack) {
                        writeCacheLog(loginUser, null, "回滚失败", dataFile);
                        throw new TaskException("ETL0023", "fileId=" + etlDatafileId + "rollback failed.");
                    }
                    return;
                }

                Integer statusCache = dataFileService.getById(etlDatafileId).getStatusCache();
                if (statusCache == DataFile.DATA_STATUS_FAILED || statusCache == DataFile.DATA_STATUS_FINISHED) {
                    logger.info("The task file were  FAILED or FINISHED");
                    return;
                }

                Map<String, Object> map = dataFileService.analyzeAndCache(dataFile, loginUser, realPath);
                Integer totalCount = (Integer) map.get("lastRowNum");
                Integer currentCount = dataFile.getSheetStartRow();
                taskDetailPojo.setTotalCount(totalCount == null ? 0 : totalCount);
                taskDetailPojo.setCurrentCount(currentCount);
                if (Checker.isNotNullOrEmpty(map.get("error"))) {
                    // 错误处理,发送告警
                    // sendCacheAlarmEmail();
                    logger.warn("Occured a exception.and send mail.");
                    throw new TaskException("ETL0022", "cache failed-->" + map.get("error"));
                }
            }
        }
        catch (TaskException e) {
            e.printStackTrace();
            logger.error("Process error.beause of:" + e.errorDesc());
            cacheAutoExecutor.removeMap(String.valueOf(dataFile.getEtlDatafileId()));
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Process error.");
            // 修改状态
            dataFile.setStatusCache(DataFile.DATA_STATUS_FAILED);
            dataFileService.update(dataFile);
            cacheAutoExecutor.removeMap(String.valueOf(dataFile.getEtlDatafileId()));
        }
    }

    /**
     * 
     * @Description: 缓存失败发送告警邮件
     * @author fangwei
     * @date 2017年6月19日 下午1:48:16
     */
    // private void sendCacheAlarmEmail() {
    // MailService mailService = SpringContextHolder.getBean(MAIL_SERVICE_NAME);
    // Email email = new Email();
    // email.setContent("缓存失败");
    // email.setTitle("缓存告警");
    // email.setResource("缓存");
    //
    // email.setSubject("缓存告警");
    // AlarmLogs alarmLogs = new AlarmLogs();
    // alarmLogs.setBusinessType(1);
    // alarmLogs.setDescription("缓存失败");
    // mailService.sendMail(email, alarmLogs);
    // }
    /**
     * 记录日志
     * 
     * @author lianhanwen
     * @date 2017年7月29日 下午5:06:01
     * @param user
     * @param totalNum
     * @param msg
     * @param dataFile
     * @throws TaskException
     */
    private void writeCacheLog(User user, Integer totalNum, String msg, DataFile dataFile) throws TaskException {
        try {
            IDataFileLogService dataFileLogService = SpringContextHolder.getBean("dataFileLogService");
            DataFileLog dataFileLog = new DataFileLog();
            dataFileLog.setAction(DataFile.ACTION_CL_CACHE_DATA);
            dataFileLog.setTotalNum(totalNum);
            if (user != null) {
                dataFileLog.setCreatedBy(user.getId());
            }
            dataFileLog.setCreated(new Date());
            dataFileLog.setEtlDatafileId(dataFile.getEtlDatafileId());
            dataFileLog.setRunning(dataFile.getStatusCache());
            dataFileLog.setSuccessNum(dataFile.getSheetStartRow());
            dataFileLog.setMessage(msg);
            dataFileLogService.save(dataFileLog);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new TaskException("ETL_ORA2000", "writeCacheLog failed");
        }
    }

}
