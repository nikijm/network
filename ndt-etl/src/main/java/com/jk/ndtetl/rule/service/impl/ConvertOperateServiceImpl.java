package com.jk.ndtetl.rule.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.rule.ConvertEntity;
import com.jk.ndtetl.rule.RuleUse;
import com.jk.ndtetl.rule.dao.ICleanOperateDao;
import com.jk.ndtetl.rule.dao.IConvertOperateDao;
import com.jk.ndtetl.rule.service.IConvertOperateService;
import com.jk.ndtetl.rule.service.IRuleUseService;
import com.jk.ndtetl.util.Checker;

/**
 * @ClassName: ITransformServiceImpl
 * @Description:
 * @author wangzhi
 * @date 2017年7月12日 上午10:25:26
 */
public class ConvertOperateServiceImpl implements IConvertOperateService {
    
	@Autowired
	ICleanOperateDao iCleanRuleDao;
	@Autowired
	IRuleUseService iRuleUseService;
	@Autowired
	IConvertOperateDao iConvertOperateDao;
	
	
	 
	@Override
	public void convert(DataFile dataFile) {
		// 查询文件清洗表和转换表
		DataFile dataFile2 = iCleanRuleDao.getCleanTableByDataFileId(dataFile.getEtlDatafileId());
		ConvertEntity convertEntity=new ConvertEntity();
		if(dataFile2 != null && dataFile2.getDataFileType() != null){
			DataFileType dataFileType = dataFile2.getDataFileType();
			RuleUse ruleUse=new RuleUse();
			ruleUse.setEtlTableId(dataFileType.getTableCleanId()!=null?dataFileType.getTableCleanId():null);
			ruleUse.setEtlTableTargetId(dataFileType.getTableConvertId()!=null?dataFileType.getTableConvertId():null);
			//查询规则映射模板  通过源表列ID排序
			List<RuleUse> listRuleSuitByTableId = iRuleUseService.listRuleSuitByTableId(ruleUse);
			List<ColumnDef> resourcesColumns = new ArrayList<>();
			List<ColumnDef> targetColumns = new ArrayList<>();
			for (RuleUse ruleUse2 : listRuleSuitByTableId) {
//				list2.add(ruleUse2.getTargetColumns());
//				list3.add(ruleUse2.getColumn());
			}
			convertEntity.setResourcesColumns(resourcesColumns);
			convertEntity.setTargetColumns(targetColumns);
		}
		//查询所有的数据
		TableDef tableClean = dataFile2.getDataFileType().getTableClean();
		TableDef tableConvert = dataFile2.getDataFileType().getTableConvert();
		convertEntity.setTableName(tableConvert.getTableName()==null?null:tableConvert.getTableName());
		//查询所有的数据  --》有序
		List<Map<String,Object>> listData =iConvertOperateDao.listCleanTableDataByTableName(tableClean);
		try {
			doConvert(listData,convertEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * @Description: 
	 * @author wangzhi
	 * @param listData 
	 * @date  2017年7月12日 下午6:21:41
	 * @param convertEntity
	 * @return void
	 */
	private void doConvert(List<Map<String, Object>> listData, ConvertEntity convertEntity) {
		if(Checker.isNotNullOrEmpty(listData)){
			//遍历所有的数据
			for (Map<String, Object> map : listData) {
				if(convertEntity==null){
					return;
				}
				List<ColumnDef> resourcesColumns = convertEntity.getResourcesColumns()!=null?convertEntity.getResourcesColumns():null;
				if (Checker.isNotNullOrEmpty(resourcesColumns)){
					//遍历所有的列
					for (ColumnDef columnDef : resourcesColumns) {
						String columnName = columnDef.getColumnName();
						Object object = map.get(columnName);
						if(object!=null){
							//将每条数据的每个列值放入每个列中
							columnDef.setColumnValue(object.toString());
						}
					}
					//保存数据
					iConvertOperateDao.saveConvertData(convertEntity);
				}
			}
		}
	}

}
