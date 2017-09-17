package com.jk.ndtetl.controller.etl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.etl.service.IValidateService;
import com.jk.ndtetl.schedule.VerifyAutoExecutor;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.exception.TaskException;
import com.jk.ndtetl.schedule.task.VerifyTask;
import com.jk.ndtetl.util.SpringContextHolder;

public class VerifyTaskImpl implements TaskRun {


	private final Logger logger = LoggerFactory.getLogger(CacheTaskImpl.class);
	@Override
	public boolean supend(String fileId){
		VerifyAutoExecutor verifyAutoExecutor = SpringContextHolder.getBean("verifyAutoExecutor");
		IValidateService validateService = SpringContextHolder.getBean("validateService");
		TaskDetailPojo vo = verifyAutoExecutor.getVo(fileId);
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
			validateService.updateFileStatus(4, dataFile);
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
		VerifyAutoExecutor verifyAutoExecutor = SpringContextHolder.getBean("verifyAutoExecutor");
		IValidateService validateService = SpringContextHolder.getBean("validateService");
		TaskDetailPojo vo = verifyAutoExecutor.getVo(fileId);
		try {
			if(vo == null){
				throw new TaskException("ETL0001","An instance cannot be found from the fileid.");
			}
			if(vo.isStop()){
				throw new TaskException("ETL0002","The task is already [stop] state.");
			}
			vo.setStop(true);
			if(vo.isSuspend()){
				DataFile df = validateService.getDataFile(fileId);
				verifyAutoExecutor.getVerifyThreadPool().execute(new VerifyTask(df, vo));
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
			DataFile dataFile = dataFileService.getById(Integer.valueOf(fileId));
			dataFile.setStatusValidate(DataFile.DATA_STATUS_READY);
			dataFile.setEtlDatafileId(Integer.valueOf(fileId));
			dataFileService.update(dataFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean reStart(String fileId){
		VerifyAutoExecutor verifyAutoExecutor = SpringContextHolder.getBean("verifyAutoExecutor");
		IValidateService validateService = SpringContextHolder.getBean("validateService");
		TaskDetailPojo vo = verifyAutoExecutor.getVo(fileId);
		try {
			if(vo == null){
				throw new TaskException("ETL0002","The file is not in the task list.");
			}
			if(!vo.isSuspend()){
				throw new TaskException("ETL0003","The status is not a suspended status.");
			}
			vo.setSecound(true);
			vo.setSuspend(false);
			DataFile df = validateService.getDataFile(fileId);
			verifyAutoExecutor.getVerifyThreadPool().execute(new VerifyTask(df, vo));
			return true;
		} catch (TaskRejectedException e) {
			logger.error("The Thread is full state.");
			logger.info(verifyAutoExecutor.getVerifyThreadPool().stat());
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
	public boolean rollBack(String fileId)  {
		try {
			VerifyAutoExecutor verifyAutoExecutor = SpringContextHolder.getBean("verifyAutoExecutor");
			IValidateService validateService = SpringContextHolder.getBean("validateService");
			DataFile dataFile = validateService.getDataFile(fileId);
			if(dataFile == null){
				throw new TaskException("ETL0001","The [dataFile] is null.");
			}
			validateService.rollBack(dataFile);
			verifyAutoExecutor.removeMap(fileId);
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
			VerifyAutoExecutor verifyAutoExecutor = SpringContextHolder.getBean("verifyAutoExecutor");
			verifyAutoExecutor.setVerifyOn(true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean stopTask(){
		try {
			VerifyAutoExecutor verifyAutoExecutor = SpringContextHolder.getBean("verifyAutoExecutor");
			verifyAutoExecutor.setVerifyOn(false);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public TaskDetailPojo getVo(Integer etlDatafileId) {
		VerifyAutoExecutor verifyAutoExecutor = SpringContextHolder.getBean("verifyAutoExecutor");
		return verifyAutoExecutor.getVo(etlDatafileId==null?"-1":etlDatafileId+"");
	}

}
