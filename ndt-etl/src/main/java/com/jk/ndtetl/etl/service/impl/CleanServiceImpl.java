package com.jk.ndtetl.etl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.dao.DataFileDao;
import com.jk.ndtetl.dbmeta.dao.TableDefDao;
import com.jk.ndtetl.etl.BaseDataEntity;
import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.etl.dao.CommonDao;
import com.jk.ndtetl.etl.service.ICleanService;
import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.rule.dao.IRuleOpDao;
import com.jk.ndtetl.schedule.conf.TurnOnOff;
import com.jk.ndtetl.schedule.exception.DataException;

@Service("cleanService")
public class CleanServiceImpl implements ICleanService {
    @Autowired
    private CommonDao commonDao;
	@Autowired
	IRuleOpDao iRuleOpDao;
	@Autowired
	DataFileDao dataFileDao;
	@Autowired
	TableDefDao tableDefDao;

	@Override
	public void cleanFlow(DataTable table, List<Integer> successIds, List<Integer> failedIds,String tableName) throws DataException {
		try {
			commonDao.saveTableContent(table);
			BaseDataEntity baseDataEntity = new BaseDataEntity();
			baseDataEntity.setTableName(tableName);
			if(successIds!=null && successIds.size() > 0){
				baseDataEntity.setResult(BaseDataEntity.RESULT_AUTO_SUCCESS);
				commonDao.updateDataByIds(baseDataEntity, successIds);
			}
			if(failedIds!=null && failedIds.size() > 0){
				baseDataEntity.setResult(BaseDataEntity.RESULT_MANUAL_FAIL);
				commonDao.updateDataByIds(baseDataEntity, failedIds);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL-ORA0035","An exception occurred inserted into the cleaning table.");
		}
	}

	@Override
	public void doClean(DataFile dataFile) throws DataException {
	}

	@Override
	public void rollBack(DataFile dataFile) throws DataException{
		try {
			commonDao.deleteByUnId(dataFile.getDataFileType().getTableClean().getTableName(), dataFile.getUnId());
			dataFile.setStatusClean(DataFile.DATA_STATUS_NOTSTART);
			dataFileDao.update(dataFile);
			BaseDataEntity baseDataEntity =new BaseDataEntity();
			baseDataEntity.setTableName(dataFile.getDataFileType().getTableCache().getTableName());
			baseDataEntity.setDatafile_unid(dataFile.getUnId());
			baseDataEntity.setResult(BaseDataEntity.RESULT_PENDING);
			commonDao.updateDataByUnId(baseDataEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL-ORA0051","RollBack make a exception.");
		}
	}

	@Override
	public void updateFileStatus(int ope,DataFile dataFile) throws DataException {
		try {
			switch (ope) {
			case 0:
				dataFile.setStatusClean(DataFile.DATA_STATUS_STARTED);
				break;
			case 1:
				dataFile.setStatusClean(DataFile.DATA_STATUS_FAILED);
				break;
			case 2:
				dataFile.setStatusClean(DataFile.DATA_STATUS_FINISHED);
				if(TurnOnOff.getInstance().isAuto()){
					dataFile.setStatusConvert(DataFile.DATA_STATUS_READY);
				}
				break;
			case 4:
				dataFile.setStatusClean(DataFile.DATA_STATUS_PAUSE);
				break;
			default:
				dataFile.setStatusClean(DataFile.DATA_STATUS_NOTSTART);
				break;
			}
			dataFileDao.update(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL-ORA0032","Update the status failed.beause of:"+e.getMessage());
		}
	}
	
	public CleanParam[] parseParams(String jsonParam) throws DataException{
		try {
			JSONObject jsonObject = JSONObject.parseObject(jsonParam);
			String json = jsonObject.getString("params");
			if(json == null || json.isEmpty()){
				return null;
			}
			JSONArray parseArray = JSON.parseArray(json);
			CleanParam[] cp = new CleanParam[parseArray.size()];
			for (int i = 0; i < parseArray.size(); i++) {
				CleanParam cleanParam = new CleanParam();
				JSONObject jsonObj = (JSONObject) parseArray.get(i);
				String paramKey = jsonObj.getString("paramKey");
				cleanParam.setParamKey(paramKey);
				String name = jsonObj.getString("name");
				cleanParam.setName(name);
				String type = jsonObj.getString("type");
				cleanParam.setType(type);
				String paramValue = jsonObj.getString("paramValue");
				cleanParam.setParamValue(paramValue);
				cp[i] = cleanParam;
			}
			return cp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("ETL9999","Make a unknown exception when parse paramJson.");
		}
	}

	@Override
	public DataFile getDataFile(String fileId) throws DataException {
		DataFile df = null;
        try {
            df = dataFileDao.getDataFile(Integer.valueOf(fileId));
            TableDef table = tableDefDao.getById(df.getDataFileType().getTableCleanId());
            df.getDataFileType().setTableClean(table);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DataException("ETL-ORA0061", "select dataFile by id failed.beause of:" + e.getMessage());
        }
        return df;
	}

}
