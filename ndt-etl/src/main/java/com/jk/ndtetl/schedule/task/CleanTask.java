package com.jk.ndtetl.schedule.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileLog;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IDataFileLogService;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.BaseDataEntity;
import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.etl.service.ICleanService;
import com.jk.ndtetl.etl.service.ICommonService;
import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.IClean;
import com.jk.ndtetl.rule.RuleOp;
import com.jk.ndtetl.rule.RuleUse;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.rule.service.IRuleOpService;
import com.jk.ndtetl.rule.service.IRuleUseService;
import com.jk.ndtetl.rule.service.IVerifyRuleService;
import com.jk.ndtetl.schedule.CleanAutoExecutor;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.schedule.exception.DataException;
import com.jk.ndtetl.util.SpringContextHolder;

public class CleanTask implements Runnable {

	private final static Logger logger = LoggerFactory.getLogger(CacheTask.class);
	private final static String COMMON_SERVICE_NAME = "commonService";
	private final static String CLEAN_AUTOEXECUTOR_NAME = "cleanAutoExecutor";
	private final static String CLEAN_SERVICE_NAME = "cleanService";
	private final static String LOG_SERVICE_NAME = "dataFileLogService";
	private final static String TABLE_SERVICE_NAME = "tableDefService";
	private final static String RULEUSE_SERVICE_NAME = "iRuleUseService";
	private final static String RULEOP_SERVICE_NAME = "iRuleOpService";
	
	private ICommonService commonService = null;
	private ICleanService cleanService = null;
	private CleanAutoExecutor cleanAutoExecutor = null;
	private IDataFileLogService dataFileLogService = null;
	private ITableDefService tableService = null;
	private IRuleUseService ruleUseService = null;
	private IRuleOpService ruleOpService = null;
	
	private DataFile dataFile = null;
	private TaskDetailPojo vo = null;
	
	private StringBuilder errorMsg = new StringBuilder();
	private List<RuleUse> listRuleUse = null;
	private List<Integer> successIds = null;
	private List<Integer> failedIds = null;
	
	private int successCount = 0;
	private int failedCount = 0;
	
	public CleanTask(DataFile df, TaskDetailPojo vo) {
		this.dataFile = df;
		this.vo = vo;
	}

	//清洗处理逻辑
	@Override
	public void run() {
		logger.info("~~~~Start cleaning the ["+dataFile.getFileName()+"] file.~~~~");
		try {
			if(dataFile == null || vo == null){
				throw new DataException("ETL0030","[dataFile] or [vo] Objects is empty.");
			}
			//初始化services
			initServices();
			
			if(vo.isStop()){
				logger.info("It's a [stop] mission.");
	    		//回滚数据
	    		cleanService.rollBack(dataFile);
	    		cleanAutoExecutor.removeMap(String.valueOf(dataFile.getEtlDatafileId()));
	    		return;
			}
			//根据源table、目标table查询规则模板，再根据模板查询具体处理器
			getRules();
			
			//修改文件状态为清洗running
			cleanService.updateFileStatus(0,dataFile);
			if(logger.isDebugEnabled()){
				logger.debug("Successfully modified the file named ["+dataFile.getFileName()+"] in the state of cleaning.");
			}
            BasePage page = getPage();
            //查询总共的数据量
            int totalCount = 0;
            int i = 0;//循环当前值
            if(vo.isSecound()){
            	logger.info("It's a [restart] mission.");
            	totalCount = vo.getTotalCount();
            	this.successCount = vo.getSuccessCount();
            	this.failedCount = vo.getFailedCount();
            	i = vo.getI();
            }else{
            	totalCount = queryCount();
            }
            if(totalCount == 0){
            	throw new DataException("ETL0030","According this ["+dataFile.getUnId()+"] fileId, data that needs to be cleaned is not found in the cache table.");
            }
            logger.info("The file ["+dataFile.getFileName()+"] has ["+totalCount+"] pieces of data that need cleaning.");
            //将总条数更新到vo对象中
            vo.setTotalCount(totalCount);
            int remainder = totalCount%page.getPage_size();
            int count = totalCount/page.getPage_size();
            int forCount= remainder != 0?count+1:count;
            for (; i < forCount; i++) {
            	//每次执行清洗操作的时候判断该任务是否停止
            	if(vo.isStop()){
            		logger.warn("The task has stoped.");
            		//回滚数据
            		cleanService.rollBack(dataFile);
            		cleanAutoExecutor.removeMap(String.valueOf(dataFile.getEtlDatafileId()));
            		return;
            	}
            	//判断此任务是否暂停
            	if(vo.isSuspend()){
            		logger.warn("The task has supended.");
            		vo.setSuccessCount(this.successCount);
            		vo.setFailedCount(this.failedCount);
            		vo.setI(i);
            		return;
            	}
    			//根据文件id分页查询需要处理的缓存总数据  
            	List<Map<String,Object>> listPage = listPage(page);
        		if(listPage == null || listPage.size() == 0){
        			logger.warn("No data was found to be cleaned.");
        			break;
        		}
        		successIds = new ArrayList<Integer>();
        		failedIds = new ArrayList<Integer>();
            	//开始清洗数据
            	DataTable dataTable = process(listPage);
            	
            	//将清洗完毕的数据插入清洗表
				cleanService.cleanFlow(dataTable, successIds, failedIds,dataFile.getDataFileType().getTableCache().getTableName());
				successCount += successIds.size();
				failedCount += failedIds.size();
            	//循环处理并实时更新数据到vo对象中
            	vo.setCurrentCount(vo.getCurrentCount()+listPage.size());
			}
            if(totalCount == successCount){
            	cleanService.updateFileStatus(2,dataFile);
            	saveLog("Success");
            	if(logger.isDebugEnabled()){
                	logger.debug("The log was logged successfully.");
                }
            	logger.info("Cleaning file ["+dataFile.getFileName()+"] successfully");
            }else{
            	if(failedCount > 0){
                	throw new DataException("ETL0040",errorMsg.toString());
                }
            	throw new DataException("ETL0034","The number of successes is not equal to the total.");
            }
		} catch (DataException e) {
			logger.error(e.errorDesc());
			try {
				cleanService.updateFileStatus(1,dataFile);
				if(!e.getErrorCode().equals("ETL-ORA2000")){
					saveLog(e.getErrorDesc());
				}
			} catch (DataException e1) {
				logger.error(e1.errorDesc());
			}
			cleanAutoExecutor.removeMap(String.valueOf(dataFile.getEtlDatafileId()));
		}
		logger.info("~~~~End cleaning the ["+dataFile.getFileName()+"] file.~~~~");
	}
	
	/**
	 * 保存日志
	 * @param msg
	 * @throws DataException
	 */
	private void saveLog(String msg) throws DataException{
		try {
			DataFileLog dataFileLog = new DataFileLog();
			dataFileLog.setAction(DataFile.ACTION_CLEAN);
			dataFileLog.setTotalNum(successCount);
			dataFileLog.setErrorNum(failedCount);
			if(vo != null){
				if(vo.getUser() != null){
					dataFileLog.setCreatedBy(vo.getUser().getId());
				}
			}
			dataFileLog.setCreated(new Date());
			dataFileLog.setEtlDatafileId(dataFile.getEtlDatafileId());
			dataFileLog.setRunning(dataFile.getStatusClean());
			dataFileLog.setMessage(msg);
			dataFileLogService.save(dataFileLog);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL-ORA2000","Maked a Exception where save log.");
		}
	}
	/**
	 * 初始化services
	 * @throws DataException
	 */
	private void initServices() throws DataException {
		commonService = SpringContextHolder.getBean(COMMON_SERVICE_NAME);
		cleanAutoExecutor = SpringContextHolder.getBean(CLEAN_AUTOEXECUTOR_NAME);
		cleanService = SpringContextHolder.getBean(CLEAN_SERVICE_NAME);
		dataFileLogService = SpringContextHolder.getBean(LOG_SERVICE_NAME);
		tableService = SpringContextHolder.getBean(TABLE_SERVICE_NAME);
		ruleUseService = SpringContextHolder.getBean(RULEUSE_SERVICE_NAME);
		ruleOpService = SpringContextHolder.getBean(RULEOP_SERVICE_NAME);
		if(commonService == null){
			throw new DataException("ETL0030","The [commonService] is null.");
		}
		if(cleanAutoExecutor == null){
			throw new DataException("ETL0030","The [cleanAutoExecutor] is null.");
		}
		if(cleanService == null){
			throw new DataException("ETL0030","The [cleanService] is null.");
		}
		if(dataFileLogService == null){
			throw new DataException("ETL0030","The [dataFileLogService] is null.");
		}
		if(tableService == null){
			throw new DataException("ETL0030","The [tableService] is null.");
		}
		if(ruleUseService == null){
			throw new DataException("ETL0030","The [ruleUseService] is null.");
		}
		if(ruleOpService == null){
			throw new DataException("ETL0030","The [ruleOpService] is null.");
		}
	}
	

	/**
	 * 查询需要缓存的数据
	 * @param page
	 * @return
	 * @throws DataException
	 */
	private List<Map<String,Object>> listPage(BasePage page) throws DataException{
		try {
			return commonService.listByPage(dataFile.getDataFileType().getTableCache(), page);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL-ORA0033","Paging query data failed.beause of:"+e.getMessage());
		}
	}
	
	/**
	 * 查询数据总量
	 * @return
	 * @throws DataException
	 */
	private int queryCount() throws DataException{
		try {
			return commonService.getCountByUnIdAndResult(dataFile.getDataFileType().getTableCache().getTableName(),dataFile.getUnId(), BaseDataEntity.RESULT_PENDING);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL-ORA0033","Failed to query total data.beause of:"+e.getMessage());
		}
	}
	
	/**
	 * 获取page对象
	 * @return
	 */
	private BasePage getPage(){
        BasePage page = new BasePage();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("unId", dataFile.getUnId());
        map.put("result", 0);
        page.setParam(map);
        return page;
	}
	
	/**
	 * 清洗数据
	 * @param listData
	 * @return
	 * @throws DataException
	 */
	private DataTable process(List<Map<String,Object>> listData) throws DataException{
		try {
			DataTable dt = addColumn();
			if((dt.getHeader().size() -2) != dataFile.getDataFileType().getTableClean().getColumns().size()){
				throw new DataException("ETL0038","Field mapping error.tablename is ["
				+ dataFile.getDataFileType().getTableCache().getTableName()+"],"
				+ "Target tablename is [" + dataFile.getDataFileType().getTableClean().getTableName()+"].");
			}
			for (Map<String, Object> map : listData) {
				List<String> listValue = new ArrayList<String>();
				Object unidObj = map.get("DATAFILE_UNID");
				Object seqNoObj = map.get("SEQNO");
				Object idObj = map.get("ID");
				if(unidObj == null || seqNoObj == null || idObj == null){
					throw new DataException("ETL0030","The [unid] or [seqNo] or [id] is null.");
				}
				listValue.add(unidObj.toString());
				listValue.add(seqNoObj.toString());
				Integer id = Integer.valueOf(idObj.toString());
				for (RuleUse ruleUse : listRuleUse) {
					String columnName = ruleUse.getColumnName();
					String value = (String)map.get(columnName);
					if(value == null){
						logger.warn("There is no ["+columnName+"] field in map.");
						continue;
					}
					if(ruleUse.getRuleSuit() == null){
						listValue.add(value);
						continue;
					}
					List<RuleOp> ruleOps = ruleUse.getRuleSuit().getRuleOps();
					if(ruleOps != null && ruleOps.size() > 0){
						try {
							value = clean(ruleOps,value);
						} catch (DataException e) {
							if(e.getErrorCode().equals("ETL3000")){
								errorMsg.append(e.getErrorDesc()).append("field ["+columnName+"];");
								failedIds.add(id);
								continue;
							}
							throw e;
						}
					}
					listValue.add(value);
				}
				dt.addContent(listValue);
				successIds.add(id);
			}
			return dt;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL9999","Make a unknown exception where process clean.");
		}
	}
	
	/**
	 * 得到datatable对象
	 * @return
	 */
	private DataTable addColumn(){
		DataTable dt = new DataTable();
		dt.addHeader("DATAFILE_UNID");
		dt.addHeader("SEQNO");
		for (RuleUse ruleUse : listRuleUse) {
			String name = ruleUse.getEtlCoulumnTargetName();
			dt.addHeader(name);
		}
		dt.setTableName(dataFile.getDataFileType().getTableClean().getTableName());
		dt.setDatafile_unid(dataFile.getUnId());
		return dt;
	}
	
	/**
	 * 清洗操作
	 * @param ruleOps
	 * @param value
	 * @return
	 * @throws DataException
	 */
	private String clean(List<RuleOp> ruleOps,String value) throws DataException{
		try {
			for (RuleOp ruleOp : ruleOps) {
				if(logger.isDebugEnabled()){
					logger.debug("Begin processing the value ["+value+"] according to rule ["+ruleOp.getName()+"].");
				}
				if(ruleOp.getIclean() instanceof IVerifyRuleService){
					IVerifyRuleService is = (IVerifyRuleService)ruleOp.getIclean();
					if(!is.verify(value, ruleOp.getCleanParams())){
						throw new DataException("ETL3000","Value ["+value+"] ["+ruleOp.getName()+"] validate failed.");
					}
					continue;
				}
				if(ruleOp.getIclean() instanceof StringOperation){
					StringOperation so = (StringOperation)ruleOp.getIclean();
					value = so.doCleans(value, ruleOp.getCleanParams());
				}
			}
			return value;
		} catch (DataException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL9999","Make a unknown exception when cleaning.");
		}
	}
	
	/**
	 * 查询规则
	 * @throws DataException
	 */
	private void getRules() throws DataException{
		try {
			TableDef tbCache = tableService.getById(dataFile.getDataFileType().getTableCacheId());
			if(tbCache == null || tbCache.getColumns() == null || tbCache.getColumns().size() == 0){
				throw new DataException("ETL-ORA0040","No data was found according to the cache table id.");
			}
			dataFile.getDataFileType().setTableCache(tbCache);
			TableDef tbClean = tableService.getById(dataFile.getDataFileType().getTableCleanId());
			if(tbClean == null || tbClean.getColumns() == null || tbClean.getColumns().size() == 0){
				throw new DataException("ETL-ORA0040","No data was found according to the clean table id.");
			}
			dataFile.getDataFileType().setTableClean(tbClean);
			if(logger.isDebugEnabled()){
				logger.debug("Queryed cached table and clean table end.");
				logger.debug("cache tablename -->["+dataFile.getDataFileType().getTableCache().getTableName()+"]. it has ["+dataFile.getDataFileType().getTableCache().getColumns().size()+"] columns.");
				logger.debug("clean tablename -->["+dataFile.getDataFileType().getTableClean().getTableName()+"]. it has ["+dataFile.getDataFileType().getTableClean().getColumns().size()+"] columns.");
			}
			this.listRuleUse = ruleUseService.listRuleUseByTables(dataFile.getDataFileType().getTableCacheId(), dataFile.getDataFileType().getTableCleanId());
			if(listRuleUse == null || listRuleUse.size() == 0){
				throw new DataException("ETL-ORA0041","No rule mapping was found according to the cache table id and clean table id.");
			}
			logger.info("Rule mapping size -->["+this.listRuleUse.size()+"].");
			for (RuleUse ruleUse : listRuleUse) {
				if(ruleUse.getRuleSuit() == null){
					continue;
				}
				String json = ruleUse.getRuleSuit().getRules();
				if(logger.isDebugEnabled()){
					logger.debug("Start parse template ["+ruleUse.getRuleSuit().getName()+"].");
				}
				List<RuleOp> list = parseJson(json);
				if(list.size() == 0){
					logger.warn("This template does not have a processor.");
					continue;
				}
				ruleUse.getRuleSuit().setRuleOps(list);
			}
			logger.info("The rule handler is resolved.");
		} catch (DataException e) {
			throw e;
		} catch (Exception e){
			e.printStackTrace();
			throw new DataException("ETL-ORA9999","Make a unknown exception when get rules.");
		}
	}
	
	/**
	 * 获取处理器
	 * @param json
	 * @return
	 * @throws DataException
	 */
	private List<RuleOp> parseJson(String json) throws DataException{
		try {
			JSONArray parseArray = JSON.parseArray(json);
			List<RuleOp> list = new ArrayList<>();
			for (Object object : parseArray) {
				JSONObject jsonObject = (JSONObject) object;
				String id = jsonObject.get("id").toString();
				if (id == null || id.isEmpty()) {
					logger.warn("The [id] is null when parse json.");
					continue;
				}
				RuleOp ruleOp = ruleOpService.getById(Integer.valueOf(id));
				if(ruleOp == null || ruleOp.getClz() == null){
					logger.warn("The [ruleOp] or [classname] is null.");
					continue;
				}
				CleanParam[] cleanParams = cleanService.parseParams(ruleOp.getParams());
				if(cleanParams == null){
					logger.warn("The [cleanParams] is null.");
					continue;
				}
				ruleOp.setCleanParams(cleanParams);
				IClean iclean = null;
				try {
					iclean = (IClean)Class.forName(ruleOp.getClz()).newInstance();
				} catch (Exception e) {
					logger.error(e.getMessage());
					continue;
				}
				ruleOp.setIclean(iclean);
				list.add(ruleOp);
			}
			return list;
		} catch(NumberFormatException e){
			e.printStackTrace();
			throw new DataException("ETL0041",e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL9999","Make a unknown exception when parse json.");
		}
	}
}