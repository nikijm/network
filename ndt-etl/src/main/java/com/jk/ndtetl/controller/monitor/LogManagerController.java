package com.jk.ndtetl.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.Constant;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.mon.LogManager;
import com.jk.ndtetl.mon.service.LogManagerService;
import com.jk.ndtetl.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @ClassName: LogManagerController
 * @Description: 日志的controller
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Controller
@RequestMapping("/api")
public class LogManagerController {

	@Autowired
	LogManagerService logManagerService;

	@RequestMapping(value="/logmanagers/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Object getOneServiceInfo(@PathVariable("id") Integer id){
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			LogManager byId = logManagerService.getById(id);
			String dateToString = DateUtils.getDateToString(new Date(byId.getDateTime()));
			byId.setDateTimeView(dateToString);
			jo.put("data", byId);
			Object object = JSON.toJSON(jo);
			WebUtils.getResponse().setStatus(200);
			return object;
		} catch (Exception e) {
			jo.put("error", "获取数据失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value="/logmanagers",method=RequestMethod.GET)
	@ResponseBody
	public Object listServiceInfo(Integer level,String content,Integer page){
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			BasePage pageObj = new BasePage();
	        if (Checker.isNotNullOrEmpty(page)) {
	            pageObj.setCurrent(page);
	        }
	        Map<String, Object> param = new HashMap<>();
	        if(level!=null&&level.equals(Constant.LOG_LEVEL_INFO)){
	        	param.put("level", Constant.LOG_LEVEL_INFO);
	        }else if(level!=null&&level.equals(Constant.LOG_LEVEL_WARN)){
	        	param.put("level", Constant.LOG_LEVEL_WARN);
	        }else if(level!=null&&level.equals(Constant.LOG_LEVEL_ERROR)){
	        	param.put("level", Constant.LOG_LEVEL_ERROR);
	        }else if(level!=null&&level.equals(Constant.LOG_LEVEL_FATAL)){
	        	param.put("level", Constant.LOG_LEVEL_FATAL);
	        }else if(level==null||level.equals(Constant.LOG_LEVEL_ALL)){
	        	param.put("level", null);
	        }
//	        if(content.contains("_")||content.contains("%")){
//	        	jo.put("error", "内容中有非法字符");
//				requestError=ErrorUtil.getRequestError(jo);
//				return requestError;
//	        }
	        if(!StringUtils.isEmpty(content)){
	        	param.put("content", "%"+CommUtil.getSpecialCharQuery(content)+"%");
	        }
	        pageObj.setParam(param);
			List<LogManager> listByPage = logManagerService.listByPage(pageObj);
			for (LogManager logManager : listByPage) {
				if(logManager.getDateTime()!=null){
					String dateToString = DateUtils.getDateToString(new Date(logManager.getDateTime()));
					logManager.setDateTimeView(dateToString);
				}
			}
			PageInfo pageInfo = new PageInfo(listByPage);
			BasePage paging = new BasePage();
	        paging.setCurrent(pageInfo.getPageNum());
	        paging.setTotal(pageInfo.getTotal());
	        Map<String, Object> map = new HashMap<>();
	        Object object = JSON.toJSON(listByPage);
	        map.put("data", object);
	        map.put(BaseSystemEntity.BASE_PAGE, paging);
	        WebUtils.getResponse().setStatus(200);
	        return map;
		} catch (Exception e) {
			e.printStackTrace();
			jo.put("error", "获取数据失败");
			requestError=ErrorUtil.getRequestError(jo);
		}
		return requestError;
	}

}
