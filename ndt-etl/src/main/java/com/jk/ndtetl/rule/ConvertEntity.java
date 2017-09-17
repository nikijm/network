package com.jk.ndtetl.rule;

import java.util.List;

import com.jk.ndtetl.dbmeta.ColumnDef;

/**
 * @ClassName: ConvertEntity
 * @Description: 
 * @author wangzhi
 * @date 2017年7月12日 下午6:01:50
 */
public class ConvertEntity {
	
	private String tableName;
	
	private List<ColumnDef> resourcesColumns;
	
	private List<ColumnDef> targetColumns;
	

	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public List<ColumnDef> getResourcesColumns() {
		return resourcesColumns;
	}


	public void setResourcesColumns(List<ColumnDef> resourcesColumns) {
		this.resourcesColumns = resourcesColumns;
	}


	public List<ColumnDef> getTargetColumns() {
		return targetColumns;
	}


	public void setTargetColumns(List<ColumnDef> targetColumns) {
		this.targetColumns = targetColumns;
	}
   
}
