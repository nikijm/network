package com.jk.ndtetl.rule.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IDataFileTypeService;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.EtlOp;
import com.jk.ndtetl.rule.CleanEntity;
import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.RuleOp;
import com.jk.ndtetl.rule.RuleSuit;
import com.jk.ndtetl.rule.RuleUse;
import com.jk.ndtetl.rule.RuleUtil;
import com.jk.ndtetl.rule.StringOperation;
import com.jk.ndtetl.rule.ValidateEntity;
import com.jk.ndtetl.rule.dao.ICleanOperateDao;
import com.jk.ndtetl.rule.dao.IRuleOpDao;
import com.jk.ndtetl.rule.service.ICleanRuleService;
import com.jk.ndtetl.rule.service.IValidateRuleService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.QueryParam;

/**
 * 
 * @ClassName: CleanRuleServiceImpl
 * @Description:清洗处理的service
 * @author wangzhi
 * @date 2017年7月4日 上午9:22:26
 *
 */
@Service("iCleanRuleService")
public class CleanRuleServiceImpl implements ICleanRuleService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IDataFileTypeService IDataFileTypeService;

	@Autowired
	ITableDefService iTableDefService;

	@Autowired
	ICleanOperateDao iCleanOperateDao;

	@Autowired
	IRuleOpDao iRuleOpDao;

	/**
	 * 
	 * @Description: 对数据做清洗操作
	 * @author wangzhi
	 * @date 2017年7月15日 下午4:30:45
	 * @param fields
	 * @param data
	 * @return
	 * @return boolean
	 */
	public boolean doClean(List<ColumnDef> fields, List<Map<String, Object>> data) {
		boolean flag = false;
		if (Checker.isNotNullOrEmpty(data) || Checker.isNotNullOrEmpty(fields)) {
			return false;
		}
		// 遍历所有的数据
		for (Map<String, Object> map : data) {
			// 遍历所有的列
			for (ColumnDef field : fields) {
				String columnName = field.getColumnName().trim() == null ? "" : field.getColumnName().trim();
				Object object = map.get(columnName);
				if (object != null && field.getRuleUse() != null) {
					RuleUse ruleUse = field.getRuleUse();
					if (ruleUse.getRuleSuit() != null) {
						RuleSuit ruleSuit = ruleUse.getRuleSuit();
						List<RuleOp> ruleOps = ruleSuit.getRuleOps() == null ? null : ruleSuit.getRuleOps();
						if (Checker.isNotNullOrEmpty(ruleOps)) {
							// 遍历所有的处理器
							for (RuleOp ruleOp : ruleOps) {
								if (ruleOp.getEtlop() != null && ruleOp.getEtlop() == TableDef.CATEGORY_CLEAN) {
									doTrueClean(object, ruleOp);
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}

	private void doTrueClean(Object object, RuleOp ruleOp) {
		String value = object.toString();
		// 规则处理器的实现类
		StringOperation cleantor = null;
		CleanEntity jsonParseToEntity = new CleanEntity();
		jsonParseToEntity.setName(ruleOp.getName());
		if (ruleOp.getCleanParam() != null) {
			jsonParseToEntity.setCleanParams(ruleOp.getCleanParam());
		}
		// if (ruleOp.getParams() != null) {
		// jsonParseToEntity =
		// RuleUtil.jsonParseToCleanEntity(ruleOp.getParams());
		// }
		if (ruleOp.getClz() != null) {
			try {
				cleantor = RuleUtil.getCleaner(ruleOp.getClz());
				String cleanerName = RuleUtil.getCleanerName(ruleOp.getClz());
				jsonParseToEntity.setOperationName(cleanerName);
			} catch (Exception e) {
				System.out.println("获取处理类失败");
				e.printStackTrace();
			}
		} 
		if (jsonParseToEntity.getCleanParams() != null) {
			// 对明个字段值做清洗
			cleantor.doClean(value, jsonParseToEntity.getCleanParams());
		}
	}

	public Boolean doValidate(List<ColumnDef> fields, List<Map<String, Object>> data) {
		boolean flag = false;
		if (Checker.isNotNullOrEmpty(data) || Checker.isNotNullOrEmpty(fields)) {
			return false;
		}
		// 遍历所有的数据
		for (Map<String, Object> map : data) {
			// 遍历所有的数据
			for (ColumnDef field : fields) {
				String columnName = field.getColumnName().trim() == null ? null : field.getColumnName().trim();
				Object object = map.get(columnName);
				if (object != null && field.getRuleUse() != null) {
					RuleUse ruleUse = field.getRuleUse();
					if (ruleUse.getRuleSuit() != null) {
						RuleSuit ruleSuit = ruleUse.getRuleSuit();
						List<RuleOp> ruleOps = ruleSuit.getRuleOps() == null ? null : ruleSuit.getRuleOps();
						if (Checker.isNotNullOrEmpty(ruleOps)) {
							// 遍历所有的处理器
							for (RuleOp ruleOp : ruleOps) {
								if (ruleOp.getEtlop() != null && ruleOp.getEtlop() == TableDef.CATEGORY_VALIDATE) {
									// 对数据做校验
									flag = doTrueValidate(object, ruleOp);
									if (flag == false) {
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}

	private boolean doTrueValidate(Object object, RuleOp ruleOp) {
		boolean flag;
		String value = object.toString();
		IValidateRuleService getvalidator = null;
		ValidateEntity jsonParseToEntity = new ValidateEntity();
		// 设置验证名称
		jsonParseToEntity.setName(ruleOp.getName());
		// 设置验证参数
		jsonParseToEntity.setCleanParam(ruleOp.getCleanParam());
		getvalidator = RuleUtil.getValidator(ruleOp.getClz());
		String validatorName = RuleUtil.getvalidatorName(ruleOp.getClz());
		// 设置处理器的名字
		jsonParseToEntity.setOperationName(validatorName);
		flag = getvalidator.doValidate(value, jsonParseToEntity.getCleanParam());
		return flag;
	}

	@Override
	public Boolean clean(DataFile dataFile) throws Exception {
		if (dataFile.getEtlDatafileId() == null) {
			return null;
		}
		// 查询需要清洗的表名
		DataFile dataFile2 = iCleanOperateDao.getCleanTableByDataFileId(dataFile.getEtlDatafileId());
		List<Map<String, Object>> data = null;
		TableDef tableDef = null;
		Boolean doClean = null;
		if (dataFile2 != null && dataFile2.getDataFileType() != null
				&& dataFile2.getDataFileType().getTableClean() != null) {
			TableDef tableCache = dataFile2.getDataFileType().getTableCache();
			TableDef tableClean = dataFile2.getDataFileType().getTableClean();
			QueryParam queryParam = getQueryParam(dataFile2, tableCache);
			// 获取清洗的数据
			PageHelper.startPage(1, 5);
			data = iCleanOperateDao.getCleanDataByParam(queryParam);
			// 获取清洗的规则
			tableDef = getCleanRule(tableClean);
			if (Checker.isNotNullOrEmpty(tableDef) && Checker.isNotNullOrEmpty(tableDef.getColumns())
					&& Checker.isNotNullOrEmpty(data)) {
				doClean = doClean(tableDef.getColumns(), data);
				if (doClean) {
					updateCleanDataStatus(data, tableCache);
				}
			}
		}
		return doClean;
	}

	/**
	 * @Description:
	 * @author wangzhi
	 * @date 2017年7月14日 下午1:05:52
	 * @param data
	 * @param tableCache
	 * @return void
	 */
	private void updateCleanDataStatus(List<Map<String, Object>> data, TableDef tableCache) {
		for (Map<String, Object> map : data) {
			Object object = map.get("id");
			if (object != null) {
				QueryParam queryParam2 = new QueryParam();
				Map<String, Object> param = queryParam2.getParam();
				param.put("id", Integer.valueOf(object.toString()).intValue());
				param.put("tableName", tableCache.getTableName());
				param.put("status", 1);
				iCleanOperateDao.updateCleanDataStatu(param);
			}
		}

	}

	@Override
	public boolean validate(DataFile dataFile) throws Exception {
		// 验证结果
		Boolean doValidate = true;
		if (dataFile.getEtlDatafileId() == null) {
			return false;
		}
		// 查询需要清洗的表名
		DataFile dataFile2 = iCleanOperateDao.getCleanTableByDataFileId(dataFile.getEtlDatafileId());
		List<Map<String, Object>> data = null;
		TableDef tableDef = null;
		if (dataFile2 != null && dataFile2.getDataFileType() != null
				&& dataFile2.getDataFileType().getTableCache() != null) {
			// 得到缓存表
			TableDef tableCache = dataFile2.getDataFileType().getTableCache();
			// 得到清洗表
			TableDef tableClean = null;
			tableClean = dataFile2.getDataFileType().getTableClean();
			// 获得缓存表的查询参数
			QueryParam queryParam = getQueryParam(dataFile2, tableCache);
			// 分页获取清洗的数据
			PageHelper.startPage(1, 5);
			data = iCleanOperateDao.getCleanDataByParam(queryParam);
			// 获取清洗的规则
			tableDef = getCleanRule(tableClean);
			if (Checker.isNotNullOrEmpty(tableDef) && Checker.isNotNullOrEmpty(tableDef.getColumns())
					&& Checker.isNotNullOrEmpty(data)) {
				doValidate = doValidate(tableDef.getColumns(), data);
				if (doValidate) {
					// 处理完了修改数据的状态
					updateVlidateDataStatus(data, tableCache);
				}
			}
		}
		return doValidate;
	}

	private void updateVlidateDataStatus(List<Map<String, Object>> data, TableDef tableCache) {
		for (Map<String, Object> map : data) {
			Object object = map.get("id");
			if (object != null) {
				QueryParam queryParam2 = new QueryParam();
				Map<String, Object> param = queryParam2.getParam();
				param.put("id", Integer.valueOf(object.toString()).intValue());
				param.put("tableName", tableCache.getTableName());
				param.put("status", 1);
				iCleanOperateDao.updateValidateDataStatu(queryParam2);
			}
		}
	}

	private TableDef getCleanRule(TableDef tableClean) {
		if (tableClean == null || tableClean.getEtlTableId() == null) {
			return null;
		}
		TableDef tableDef;
		tableDef = iTableDefService.getRuleByTableId(tableClean.getEtlTableId());
		if (tableDef != null && tableDef.getColumns() != null && tableDef.getColumns().size() > 0) {
			List<ColumnDef> columns = tableDef.getColumns();
			for (ColumnDef columnDef : columns) {
				if (Checker.isNotNullOrEmpty(columnDef.getRuleUse())
						&& Checker.isNotNullOrEmpty(columnDef.getRuleUse().getRuleSuit())) {
					RuleSuit ruleSuit = columnDef.getRuleUse().getRuleSuit();
					String rules = ruleSuit.getRules();
					List<RuleOp> list = new ArrayList<>();
					JSONArray parseArray = JSON.parseArray(rules);
					for (Object object : parseArray) {
						JSONObject jsonObject = (JSONObject) object;
						Object object2 = jsonObject.get("id");
						RuleOp byId = null;
						if (object2 != null) {
							byId = iRuleOpDao.getById(Integer.valueOf(object2.toString()).intValue());
							Object object3 = jsonObject.get("params");
							if (object3 != null) {
								JSONArray parseArray2 = JSON.parseArray(object3.toString());
								if (parseArray2.size() > 0 && parseArray2.size() == 1) {
									CleanParam cleanParam = new CleanParam();
									for (Object object4 : parseArray2) {
										cleanParam.setParamOne(object4.toString());
									}
									byId.setCleanParam(cleanParam);
								} else if (parseArray2.size() > 0 && parseArray2.size() == 2) {
									CleanParam cleanParam = new CleanParam();
									boolean flag = true;
									for (Object object4 : parseArray2) {
										if (flag == true) {
											cleanParam.setParamOne(object4.toString());
											flag = false;
										}
										cleanParam.setParamTwo(object4.toString());
									}
									byId.setCleanParam(cleanParam);
								} else {
									System.out.println("参数有误");
								}
							}
						}
						list.add(byId);
					}
					ruleSuit.setRuleOps(list);
				}

			}
		}
		return tableDef;
	}

	private QueryParam getQueryParam(DataFile dataFile2, TableDef tableClean) {
		QueryParam queryParam = new QueryParam();
		Map<String, Object> param = queryParam.getParam();
		if (tableClean.getTableName() != null) {
			param.put("tableName", tableClean.getTableName());
		}
		if (dataFile2.getEtlDatafileId() != null) {
			param.put("dataFileId", dataFile2.getEtlDatafileId());
		}
		return queryParam;
	}

	@Override
	public Long getCountNoValidate(QueryParam queryParam) {
		Long count = iCleanOperateDao.getCountNoValidate(queryParam);
		return count;
	}

	@Override
	public void updateCleanValidateStatus(QueryParam queryParam2) {
		iCleanOperateDao.updateCleanValidateStatus(queryParam2);
	}

	@Override
	public Long getCountNoClean(QueryParam queryParam) {
		Long count = iCleanOperateDao.getCountNoClean(queryParam);
		return count;
	}

	@Override
	public void updateCleanStatus(QueryParam queryParam2) {
		iCleanOperateDao.updateCleanStatus(queryParam2);
	}

	@Override
	public void updateValidateStatuByTableId(QueryParam queryParam2) {
		iCleanOperateDao.updateValidateStatuByTableId(queryParam2);
	}

	@Override
	public void updateCleanDataByTableId(QueryParam queryParam2) {
		iCleanOperateDao.updateCleanDataByTableId(queryParam2);
	}

	@Override
	public long getCountAll(QueryParam queryParam3) {
		long count = iCleanOperateDao.getCountAll(queryParam3);
		return count;
	}

	@Override
	public long getCountCleaned(QueryParam queryParam4) {
		long count = iCleanOperateDao.getCountCleaned(queryParam4);
		return count;
	}

}
