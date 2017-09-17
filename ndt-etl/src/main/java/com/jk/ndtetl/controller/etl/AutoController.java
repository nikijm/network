package com.jk.ndtetl.controller.etl;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.etl.bean.TaskPojo;
import com.jk.ndtetl.controller.etl.task.TaskRun;
import com.jk.ndtetl.schedule.AutoExecutor;
import com.jk.ndtetl.schedule.util.ScheduleUtil;
import com.jk.ndtetl.util.StringUtils;

@Controller
@RequestMapping(value = "/api")
public class AutoController extends BaseController {

	private static final String FAILED="-1";
	private static final String SUCCESS="0";

	@Resource(name="autoExecutor")
	AutoExecutor autoExecutor;

	/**
	 * 暂停单个任务
	 * @param taskPojo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/schedule/supend", method = RequestMethod.GET)
	public @ResponseBody Object forSupend(TaskPojo taskPojo, HttpServletRequest request) {
		TaskRun tr = null;
		String fileId = null;
		try {
			checkParam(taskPojo);
			tr = autoExecutor.getTaskRun(taskPojo.getOperateId());
			if(tr == null){
				throw new Exception("An instance cannot be found from the action type.");
			}
			fileId = taskPojo.getFileId();
			if(!tr.supend(fileId)){
				throw new Exception("Pause failed.");
			}
		} catch (Exception e) {
			logger.error("End supended,beause of:"+e.getMessage()+",The time is:" + ScheduleUtil.format(new Date()));
			return FAILED;
		}
		logger.info("Supended successful.The time is:" + ScheduleUtil.format(new Date()));
		return SUCCESS;
	}


	/**
	 * 停止单个文件，将不再进行新的任务处理
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/schedule/stop", method = RequestMethod.GET)
	public @ResponseBody Object forStop(TaskPojo taskPojo, HttpServletRequest request) {
		TaskRun tr = null;
		String fileId = null;
		try {
			checkParam(taskPojo);
			tr = autoExecutor.getTaskRun(taskPojo.getOperateId());
			if(tr == null){
				throw new Exception("An instance cannot be found from the action type.");
			}
			fileId = taskPojo.getFileId();
			if(!tr.stop(fileId)){
				throw new Exception("Stop failed.");
			}
		} catch (Exception e) {
			logger.error("Stop failed,beause of:"+e.getMessage()+",The time is:" + ScheduleUtil.format(new Date()));
			return FAILED;
		}
		logger.info("Stop successed.The time is:" + ScheduleUtil.format(new Date()));
		return SUCCESS;
	}

	private void checkParam(TaskPojo taskPojo) throws Exception{
		if(taskPojo == null){
			throw new Exception("The Object [taskPojo] is null.");
		}
		if(taskPojo.getOperateId() == null || taskPojo.getOperateId().isEmpty()){
			throw new Exception("The operate id is null.");
		}
		if(taskPojo.getOperateType() == null || taskPojo.getOperateType().isEmpty()){
			throw new Exception("The operate type is null.");
		}
		if(taskPojo.getFileId() == null || taskPojo.getFileId().isEmpty()){
			throw new Exception("The file id is null.");
		}
	}

	/**
	 * 启动单个任务（左边列表 “启动”
	 * @param taskPojo
	 * @return
	 */
	@RequestMapping(value = "/schedule/start", method = RequestMethod.GET)
	public @ResponseBody Object forStart(TaskPojo taskPojo) {
		TaskRun tr = null;
		String fileId = null;
		try {
			checkParam(taskPojo);
			tr = autoExecutor.getTaskRun(taskPojo.getOperateId());
			if(tr == null){
				throw new Exception("An instance cannot be found from the action type.");
			}
			fileId = taskPojo.getFileId();
			if(!tr.start(fileId)){
				throw new Exception("start failed.");
			}
		} catch (Exception e) {
			logger.error("start failed,beause of:"+e.getMessage()+",The time is:" + ScheduleUtil.format(new Date()));
			return FAILED;
		}
		logger.info("start successed.The time is:" + ScheduleUtil.format(new Date()));
		return SUCCESS;
	}

	/**
	 * restart某一个任务（右边的启动
	 * @param taskPojo
	 * @return
	 */
	@RequestMapping(value = "/schedule/restart", method = RequestMethod.GET)
	public @ResponseBody Object forReStart(TaskPojo taskPojo) {
		TaskRun tr = null;
		String fileId = null;
		try {
			checkParam(taskPojo);
			tr = autoExecutor.getTaskRun(taskPojo.getOperateId());
			if(tr == null){
				throw new Exception("An instance cannot be found from the action type.");
			}
			fileId = taskPojo.getFileId();
			if(!tr.reStart(fileId)){
				throw new Exception("Restart failed.");
			}
		} catch (Exception e) {
			logger.error("Restart failed,beause of:"+e.getMessage()+",The time is:" + ScheduleUtil.format(new Date()));
			return FAILED;
		}
		logger.info("reStart successed.The time is:" + ScheduleUtil.format(new Date()));
		return SUCCESS;
	}



	/**
	 * stop or start某一个队列(总开关
	 * @param operateId 缓存1  清洗2  转换3  校验4
	 * @param isStop 是否是停止操作  true 是
	 * @return
	 */
	@RequestMapping(value = "/schedule/opetask", method = RequestMethod.GET)
	public @ResponseBody Object forStopTask(String operateId,Boolean isStop) {
		try {
			if (StringUtils.isBlank(operateId)) {
				throw new Exception("The Object [category] is null.");
			}
			if (null==isStop) {
				throw new Exception("The Object [isStop] is null.");
			}
			TaskRun tr = autoExecutor.getTaskRun(operateId);
			if(tr == null){
				throw new Exception("An instance cannot be found from the action type.");
			}
			if(isStop){
				if(!tr.stopTask()){
					throw new Exception("Stop failed.");
				}
			}else{
				if(!tr.startTask()){
					throw new Exception("Start failed.");
				}
			}
		} catch (Exception e) {
			logger.error("Operate task failed,beause of:"+e.getMessage()+",The time is:" + ScheduleUtil.format(new Date()));
			return FAILED;
		}
		logger.info("Operate task successed.The time is:" + ScheduleUtil.format(new Date()));
		return SUCCESS;
	}

}
