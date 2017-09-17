package com.jk.ndtetl.controller.etl.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class TaskPojo {
	//操作的类型  缓存1 清洗2 转换3 校验4
	@JSONField(name = "operateId")
	private String operateId = null;
	
	@JSONField(name = "operateType")
	private String operateType = null;
	//文件的id
	@JSONField(name = "fileId")
	private String fileId = null;

	public String getOperateId() {
		return operateId;
	}

	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
}
