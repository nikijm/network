package com.jk.ndtetl.controller.etl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.etl.service.ICleanService;
import com.jk.ndtetl.schedule.CleanAutoExecutor;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.exception.TaskException;
import com.jk.ndtetl.schedule.task.CleanTask;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.SpringContextHolder;

public class CleanTaskImpl implements TaskRun {
	
	private final Logger logger = LoggerFactory.getLogger(CacheTaskImpl.class);
	@Override
	public boolean supend(String fileId){
		CleanAutoExecutor cleanAutoExecutor = SpringContextHolder.getBean("cleanAutoExecutor");
		ICleanService cleanService = SpringContextHolder.getBean("cleanService");
		TaskDetailPojo vo = cleanAutoExecutor.getVo(fileId);
		try {
			if(vo == null){
				throw new TaskException("ETL0001","An instance cannot be found from the fileid.");
			}
			if(vo.isSuspend()){
				throw new TaskException("ETL0002","The task is already [suspend] state.");
			}
			vo.setSuspend(true);
			DataFile dataFile = new DataFile();
			dataFile.setEtlDatafileId(Integer.parseInt(fileId));
			cleanService.updateFileStatus(4, dataFile);
			return true;
		} catch (TaskException e){
			logger.error(e.errorDesc());
			if(e.getErrorCode().equals("ETL0002")){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rollBack(fileId)){
			logger.info("Rollback success.");
		}
		return false;
	}

	@Override
	public boolean stop(String fileId){
		CleanAutoExecutor cleanAutoExecutor = SpringContextHolder.getBean("cleanAutoExecutor");
		ICleanService cleanService = SpringContextHolder.getBean("cleanService");
		TaskDetailPojo vo = cleanAutoExecutor.getVo(fileId);
		try {
			if(vo == null){
				throw new TaskException("ETL0001","An instance cannot be found from the fileid.");
			}
			if(vo.isStop()){
				throw new TaskException("ETL0002","The task is already [stop] state.");
			}
			vo.setStop(true);
			if(vo.isSuspend()){
				DataFile df = cleanService.getDataFile(fileId);
				cleanAutoExecutor.getCleanThreadPool().execute(new CleanTask(df, vo));
			}
			return true;
		} catch(TaskException e){
			logger.error(e.errorDesc());
			if(e.getErrorCode().equals("ETL0001")){
				rollBack(fileId);
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof NullPointerException){
				rollBack(fileId);
			}
			return false;
		}
		
	}

	@Override
	public boolean start(String fileId){
        try {
			IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
			if(!Checker.isNum(fileId)){
				throw new TaskException("ETL0001","The fileId is not a number.");
			}
			DataFile dataFile = new DataFile();
			dataFile.setStatusClean(DataFile.DATA_STATUS_READY);
			dataFile.setEtlDatafileId(Integer.valueOf(fileId));
			dataFileService.update(dataFile);
			return true;
		} catch(TaskException e){
			logger.error(e.errorDesc());
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean reStart(String fileId){
		CleanAutoExecutor cleanAutoExecutor = SpringContextHolder.getBean("cleanAutoExecutor");
		ICleanService cleanService = SpringContextHolder.getBean("cleanService");
		TaskDetailPojo vo = cleanAutoExecutor.getVo(fileId);
		try {
			if(vo == null){
				throw new TaskException("ETL0002","The file is not in the task list.");
			}
			if(!vo.isSuspend()){
				throw new TaskException("ETL0003","The status is not a suspended status.");
			}
			vo.setSecound(true);
			vo.setSuspend(false);
			DataFile df = cleanService.getDataFile(fileId);
			cleanAutoExecutor.getCleanThreadPool().execute(new CleanTask(df, vo));
			return true;
		} catch (TaskRejectedException e) {
			logger.error("The Thread is full state.");
			logger.info(cleanAutoExecutor.getCleanThreadPool().stat());
			e.printStackTrace();
			return false;
		} catch (TaskException e){
			logger.error(e.errorDesc());
			if(e.getErrorCode().equals("ETL0002")){
				rollBack(fileId);
			}
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean rollBack(String fileId){
		try {
			CleanAutoExecutor cleanAutoExecutor = SpringContextHolder.getBean("cleanAutoExecutor");
			ICleanService cleanService = SpringContextHolder.getBean("cleanService");
			DataFile dataFile = cleanService.getDataFile(fileId);
			cleanService.rollBack(dataFile);
			cleanAutoExecutor.removeMap(fileId);
			return true;
		} catch(TaskException e){
			logger.error(e.errorDesc());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean startTask(){
		try {
			CleanAutoExecutor cleanAutoExecutor = SpringContextHolder.getBean("cleanAutoExecutor");
			cleanAutoExecutor.setCleanOn(true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stopTask(){
		try {
			CleanAutoExecutor cleanAutoExecutor = SpringContextHolder.getBean("cleanAutoExecutor");
			cleanAutoExecutor.setCleanOn(false);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public TaskDetailPojo getVo(Integer etlDatafileId) {
		CleanAutoExecutor cleanAutoExecutor = SpringContextHolder.getBean("cleanAutoExecutor");
		return cleanAutoExecutor.getVo(etlDatafileId==null?"-1":etlDatafileId+"");
	}
}
