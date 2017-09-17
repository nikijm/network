package com.jk.ndtetl.etl.service;

import java.util.List;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.etl.DataTable;
import com.jk.ndtetl.rule.CleanParam;
import com.jk.ndtetl.schedule.exception.DataException;

public interface ICleanService {
	
	DataFile getDataFile(String fileId) throws DataException;
	
	void doClean(DataFile dataFile) throws DataException;
	
	void cleanFlow(DataTable table,List<Integer> successIds,List<Integer> failedIds,String tableName) throws DataException;
	
	void rollBack(DataFile dataFile) throws DataException;
	
	void updateFileStatus(int ope,DataFile dataFile) throws DataException;
	
	CleanParam[] parseParams(String jsonParam) throws DataException;
	
}
