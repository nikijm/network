package com.jk.ndtetl.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.conf.TurnOnOff;
import com.jk.ndtetl.schedule.exception.TaskException;
import com.jk.ndtetl.schedule.pool.EtlThreadPoolTaskExecutor;
import com.jk.ndtetl.schedule.task.ConvertTask;
import com.jk.ndtetl.schedule.util.ScheduleUtil;
import com.jk.ndtetl.util.QueryParam;

@Service("transFormAutoExecutor")
public class TransFormAutoExecutor {
	
	private final static Logger logger = LoggerFactory.getLogger(TransFormAutoExecutor.class);
	Map<String, TaskDetailPojo> transFormMap = new HashMap<String, TaskDetailPojo>(8);
	TurnOnOff on = TurnOnOff.getInstance();
	private boolean isConvertOn = false;
	@Resource(name = "transformThreadPool")
	private EtlThreadPoolTaskExecutor transformThreadPool;
    @Autowired
    private IDataFileService iDataFileService;
	
//    @Scheduled(cron = "30 * * * * ?")
	public void autoTransFormExecute() {
		logger.info("======================Start converting scan======================");
		try {
			if(on.isAuto()){
				logger.info("The ProcessGram is auto.");
//				process();
//				ScheduleUtil.clear(transFormMap);
//				return;
			}
			if(isConvertOn){
				process();
				ScheduleUtil.clear(transFormMap);
				logger.info("The cleanMap size is:"+transFormMap.size());
				logger.info(transformThreadPool.stat());
			}else{
				logger.info("The Queue is not ON.");
			}
		} catch (TaskException e) {
			logger.error(e.errorDesc());
		}
		logger.info("======================End of converting scan.======================");
	}
	
	private void process() throws TaskException{
		List<DataFile> list = iDataFileService.listFileToProcess(getQueryParam());
		if(list == null){
			throw new TaskException("ETL0030","Data was not found from the database.");
		}
		for (int i = 0; i < list.size(); i++) {
			DataFile df = list.get(i);
			TaskDetailPojo vo = ScheduleUtil.createVo(0,0 ,0, String.valueOf(df.getEtlDatafileId()), false,df.getFileName());
			if(vo == null){
				logger.error("create vo Object failed. beause of：The param is illgeal.");
				continue;
			}
			
			if(transFormMap.containsKey(String.valueOf(df.getEtlDatafileId()))){
				logger.warn("The file is processing.");
				continue;
			}
			if(transformThreadPool.getActiveCount()>5){
				logger.warn("There are 5 excuting in ThreadPool.");
				return;
			}
			try {
				transformThreadPool.execute(new ConvertTask(df,vo));
				transFormMap.put(String.valueOf(df.getEtlDatafileId()), vo);
			} catch (TaskRejectedException e) {
				logger.error("The Thread is full state.");
				logger.info(transformThreadPool.stat());
				e.printStackTrace();
				break;
			} catch (Exception e){
				logger.error("Make a unknown Exception.");
				e.printStackTrace();
				throw new TaskException("ETL9999","Make a unknown Exception.");
			}
		}
	}
	
	public boolean isConvertOn() {
		return isConvertOn;
	}

	public void setConvertOn(boolean isConvertOn) {
		this.isConvertOn = isConvertOn;
	}
	public void removeMap(String key){
		transFormMap.remove(key);
	}
	public TaskDetailPojo getVo(String key){
		return transFormMap.get(key);
	}
	
	private QueryParam getQueryParam(){
		QueryParam queryParam=new QueryParam();
		Map<String, Object> param = queryParam.getParam();
		param.put("statusConvert", DataFile.DATA_STATUS_READY);
		return queryParam;
	}
	public Map<String, TaskDetailPojo> getTransFormMap() {
		return transFormMap;
	}

	public EtlThreadPoolTaskExecutor getTransformThreadPool() {
		return transformThreadPool;
	}
}
