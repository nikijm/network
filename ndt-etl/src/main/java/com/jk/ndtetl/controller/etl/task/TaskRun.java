package com.jk.ndtetl.controller.etl.task;

import com.jk.ndtetl.schedule.conf.TaskDetailPojo;

public interface TaskRun {
	
	boolean supend(String fileId);
	boolean stop(String fileId);
	boolean start(String fileId);
	boolean reStart(String fileId);
	boolean rollBack(String fileId);
	boolean startTask();
	boolean stopTask();
	TaskDetailPojo getVo(Integer etlDatafileId);
}
