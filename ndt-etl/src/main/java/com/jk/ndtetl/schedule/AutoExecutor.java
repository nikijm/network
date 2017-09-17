package com.jk.ndtetl.schedule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.jk.ndtetl.controller.etl.task.CacheTaskImpl;
import com.jk.ndtetl.controller.etl.task.CleanTaskImpl;
import com.jk.ndtetl.controller.etl.task.ConvertTaskImpl;
import com.jk.ndtetl.controller.etl.task.TaskRun;
import com.jk.ndtetl.controller.etl.task.VerifyTaskImpl;

@Service("autoExecutor")
public class AutoExecutor implements InitializingBean, ServletContextAware{
	
	private Map<String,TaskRun> map= null;

	@Override
	public void afterPropertiesSet() throws Exception {
		map = new HashMap<String,TaskRun>(4);
		map.put("1", new CacheTaskImpl());
		map.put("2", new CleanTaskImpl());
		map.put("3", new ConvertTaskImpl());
		map.put("4", new VerifyTaskImpl());
	}
	
	public TaskRun getTaskRun(String key){
		return map.get(key);
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
	}
}
