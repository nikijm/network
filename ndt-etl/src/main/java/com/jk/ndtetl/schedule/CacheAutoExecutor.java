package com.jk.ndtetl.schedule;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.conf.TurnOnOff;
import com.jk.ndtetl.schedule.exception.TaskException;
import com.jk.ndtetl.schedule.pool.EtlThreadPoolTaskExecutor;
import com.jk.ndtetl.schedule.task.CacheTask;
import com.jk.ndtetl.schedule.util.ScheduleUtil;
import com.jk.ndtetl.util.QueryParam;

@Component("cacheAutoExecutor")
public class CacheAutoExecutor{
	
	private final static Logger logger = LoggerFactory.getLogger(CacheAutoExecutor.class);
	//任务队列描述map
	Map<String, TaskDetailPojo> cacheMap = new ConcurrentHashMap<String, TaskDetailPojo>(8);
	//自动和手动模式的切换实列
	TurnOnOff on = TurnOnOff.getInstance();
	//缓存总开关
	private boolean isCacheOn = false;
	
	@Resource(name = "cacheThreadPool")
	private EtlThreadPoolTaskExecutor cacheThreadPool;
	
    @Autowired
    private IDataFileService iDataFileService;

//	@Scheduled(cron = "30 * * * * ?")
	public void autoCacheExecute() {
		logger.info("======================Start caching scan======================");
		try {
			if(on.isAuto()){
				logger.info("The ProcessGram is auto.");
//				process();
//				ScheduleUtil.clear(cacheMap);
//				return;
			}
			if(isCacheOn){
				process();
				ScheduleUtil.clear(cacheMap);
			}else{
				logger.debug("The Queue is not ON.");
			}
		} catch (TaskException e) {
			logger.error(e.errorDesc());
		}
		logger.info("The cacheMap size is:"+cacheMap.size());
		logger.info(cacheThreadPool.stat());
		logger.info("======================End of caching scan.======================");
	}
	
	private void process() throws TaskException{
		//找到需要缓存的文件，只找为ready状态的文件
		List<DataFile> list = iDataFileService.listByCacheStatus(getQueryParam());
		if(list == null){
			throw new TaskException("ETL0030","Data was not found from the database.");
		}
		if(list.size() == 0){
			logger.warn("There is no cache file to be processed for the time being.");
			return;
		}
		logger.info("There are currently ["+list.size()+"] files that need cacheing.");
		for (int i = 0; i < list.size(); i++) {
			DataFile df = list.get(i);
			//创建一个vo对象，用于实时显示前台信息
			TaskDetailPojo vo = ScheduleUtil.createVo(0,0 ,0, String.valueOf(df.getEtlDatafileId()), false,df.getFileName());
			if(vo == null){
				logger.error("Create vo Object failed. beause of:The param is illgeal.");
				continue;
			}
			
			//判断目前任务中是否有此文件ID
			if(cacheMap.containsKey(String.valueOf(df.getEtlDatafileId()))){
				logger.warn("The file is processing.");
				continue;
			}
			//判断线程池中目前处理数量，如果超过5条，则不往线程池里面添加处理
			if(cacheThreadPool.getActiveCount() > 5){
				logger.warn("There are 5 excuting in ThreadPool.");
				return;
			}
			try {
				//提交给线程池处理
				cacheThreadPool.execute(new CacheTask(df,vo));
				//将vo记录在map中
				cacheMap.put(String.valueOf(df.getEtlDatafileId()), vo);
			} catch (TaskRejectedException e) {
				logger.error("The Thread is full state.");
				logger.info(cacheThreadPool.stat());
				e.printStackTrace();
				break;
			} catch (Exception e){
				e.printStackTrace();
				throw new TaskException("ETL9999","Make a unknown Exception.");
			}
		}
	}

	public boolean isCacheOn() {
		return isCacheOn;
	}

	public void setCacheOn(boolean isCacheOn) {
		this.isCacheOn = isCacheOn;
	}
	
	public void removeMap(String key){
		cacheMap.remove(key);
	}
	
	public TaskDetailPojo getVo(String key){
		return cacheMap.get(key);
	}
	
	private QueryParam getQueryParam(){
		QueryParam queryParam=new QueryParam();
		Map<String, Object> param = queryParam.getParam();
		param.put("statusCache", DataFile.DATA_STATUS_READY);
		return queryParam;
	}

	public Map<String, TaskDetailPojo> getCacheMap() {
		return cacheMap;
	}
}
