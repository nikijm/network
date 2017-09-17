package com.jk.ndtetl.controller.etl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.etl.service.IConvertService;
import com.jk.ndtetl.schedule.TransFormAutoExecutor;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.exception.TaskException;
import com.jk.ndtetl.schedule.task.ConvertTask;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.SpringContextHolder;

public class ConvertTaskImpl implements TaskRun {


	private final Logger logger = LoggerFactory.getLogger(CacheTaskImpl.class);
	@Override
	public boolean supend(String fileId){
		TransFormAutoExecutor transFormAutoExecutor = SpringContextHolder.getBean("transFormAutoExecutor");
		IConvertService convertService = SpringContextHolder.getBean("convertService");
		TaskDetailPojo vo = transFormAutoExecutor.getVo(fileId);
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
			convertService.updateFileStatus(4, dataFile);
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
		TransFormAutoExecutor transFormAutoExecutor = SpringContextHolder.getBean("transFormAutoExecutor");
		IConvertService convertService = SpringContextHolder.getBean("convertService");
		TaskDetailPojo vo = transFormAutoExecutor.getVo(fileId);
		try {
			if(vo == null){
				throw new TaskException("ETL0001","An instance cannot be found from the fileid.");
			}
			if(vo.isStop()){
				throw new TaskException("ETL0002","The task is already [stop] state.");
			}
			vo.setStop(true);
			if(vo.isSuspend()){
				DataFile df = convertService.getDataFile(fileId);
				transFormAutoExecutor.getTransformThreadPool().execute(new ConvertTask(df, vo));
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
        	if(!Checker.isNum(fileId)){
				throw new TaskException("ETL0001","The fileId is not a number.");
			}
			IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
			DataFile dataFile = dataFileService.getById(Integer.valueOf(fileId));
			dataFile.setStatusConvert(DataFile.DATA_STATUS_READY);
			dataFileService.update(dataFile);
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
	public boolean reStart(String fileId){
		TransFormAutoExecutor transFormAutoExecutor = SpringContextHolder.getBean("transFormAutoExecutor");
		IConvertService convertService = SpringContextHolder.getBean("convertService");
		TaskDetailPojo vo = transFormAutoExecutor.getVo(fileId);
		try {
			if(vo == null){
				throw new TaskException("ETL0002","The file is not in the task list.");
			}
			vo.setSecound(true);
			vo.setSuspend(false);
			DataFile df = convertService.getDataFile(fileId);
			transFormAutoExecutor.getTransformThreadPool().execute(new ConvertTask(df, vo));
			return true;
		} catch (TaskRejectedException e) {
			logger.error("The Thread is full state.");
			logger.info(transFormAutoExecutor.getTransformThreadPool().stat());
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
	public boolean rollBack(String fileId) {
		try {
			TransFormAutoExecutor transFormAutoExecutor = SpringContextHolder.getBean("transFormAutoExecutor");
			IConvertService convertService = SpringContextHolder.getBean("convertService");
			DataFile dataFile = convertService.getDataFile(fileId);
			if(dataFile == null){
				throw new TaskException("ETL0001","The [dataFile] is null.");
			}
			convertService.rollBack(dataFile);
			transFormAutoExecutor.removeMap(fileId);
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
	public boolean startTask() {
		try {
			TransFormAutoExecutor transFormAutoExecutor = SpringContextHolder.getBean("transFormAutoExecutor");
			transFormAutoExecutor.setConvertOn(true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	public boolean stopTask() {
		try {
			TransFormAutoExecutor transFormAutoExecutor = SpringContextHolder.getBean("transFormAutoExecutor");
			transFormAutoExecutor.setConvertOn(false);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public TaskDetailPojo getVo(Integer etlDatafileId) {
		TransFormAutoExecutor transFormAutoExecutor = SpringContextHolder.getBean("transFormAutoExecutor");
		return transFormAutoExecutor.getVo(etlDatafileId==null?"-1":etlDatafileId+"");
	}
}
