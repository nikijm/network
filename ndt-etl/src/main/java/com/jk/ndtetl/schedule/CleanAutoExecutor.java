package com.jk.ndtetl.schedule;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import com.jk.ndtetl.schedule.task.CleanTask;
import com.jk.ndtetl.schedule.util.ScheduleUtil;
import com.jk.ndtetl.util.QueryParam;

@Service("cleanAutoExecutor")
public class CleanAutoExecutor {
	private final static Logger logger = LoggerFactory.getLogger(CleanAutoExecutor.class);
	Map<String, TaskDetailPojo> cleanMap = new ConcurrentHashMap<String, TaskDetailPojo>(8);
	TurnOnOff on = TurnOnOff.getInstance();
	private boolean isCleanOn = false;
	
	@Resource(name = "cleanThreadPool")
	private EtlThreadPoolTaskExecutor cleanThreadPool;
	
    @Autowired
    private IDataFileService iDataFileService;
    
//	@Scheduled(cron = "30 * * * * ?")
	public void autoCleanExecute() {
		logger.info("======================Start cleaning scan======================");
		try {
			if(on.isAuto()){
				logger.info("The ProcessGram is auto.");
//				process();
//				ScheduleUtil.clear(cleanMap);
//				return;
			}
			if(isCleanOn){
				process();
				ScheduleUtil.clear(cleanMap);
				logger.info("The cleanMap size is:"+cleanMap.size());
				logger.info(cleanThreadPool.stat());
			}else{
				logger.info("The Queue is not ON.");
			}
		} catch (TaskException e) {
			logger.error(e.errorDesc());
		}
		logger.info("======================End of cleaning scan.======================");
	}
	
	private void process() throws TaskException{
		List<DataFile> list = iDataFileService.listFileToProcess(getQueryParam());
		if(list == null){
			throw new TaskException("ETL0030","Data was not found from the database.");
		}
		if(list.size() == 0){
			logger.warn("There is no clean file to be processed for the time being.");
			return;
		}
		logger.info("There are currently ["+list.size()+"] files that need cleaning.");
		for (int i = 0; i < list.size(); i++) {
			DataFile df = list.get(i);
			if(df == null){
				throw new TaskException("ETL0015","The [datafile] is null.");
			}
			TaskDetailPojo vo = ScheduleUtil.createVo(0,0,0,String.valueOf(df.getEtlDatafileId()),false,df.getFileName());
			if(vo == null){
				logger.error("Create vo Object failed. beause of:The param is illgeal.");
				continue;
			}
			if(cleanMap.containsKey(String.valueOf(df.getEtlDatafileId()))){
				logger.warn("The file is processing now.");
				continue;
			}
			
			if(cleanThreadPool.getActiveCount() > 5){
				logger.warn("There are 5 excuting in ThreadPool");
				return;
			}
			try {
				//提交给线程池处理
				cleanThreadPool.execute(new CleanTask(df,vo));
				//将vo记录在map中
				cleanMap.put(String.valueOf(df.getEtlDatafileId()), vo);
			} catch (TaskRejectedException e) {
				logger.error("The Thread is full state.");
				logger.info(cleanThreadPool.stat());
				e.printStackTrace();
				break;
			} catch (Exception e){
				e.printStackTrace();
				throw new TaskException("ETL9999","Make a unknown Exception.");
			}
		}
	}
	
	public boolean isCleanOn() {
		return isCleanOn;
	}

	public void setCleanOn(boolean isCleanOn) {
		this.isCleanOn = isCleanOn;
	}

	public void removeMap(String key){
		cleanMap.remove(key);
	}
	
	public TaskDetailPojo getVo(String key){
		return cleanMap.get(key);
	}

	private QueryParam getQueryParam(){
		QueryParam queryParam=new QueryParam();
		queryParam.getParam().put("statusClean", DataFile.DATA_STATUS_READY);
		return queryParam;
	}

	public Map<String, TaskDetailPojo> getCleanMap() {
		return cleanMap;
	}

	public EtlThreadPoolTaskExecutor getCleanThreadPool() {
		return cleanThreadPool;
	}
}
