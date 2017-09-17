package com.jk.ndtetl.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.mon.ServiceInfo;
import com.jk.ndtetl.mon.service.ServiceManagerService;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: ServiceManagerController
 * @Description: 服务管理的controller
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Controller
@RequestMapping("/api")
public class ServiceManagerController {

	@Autowired
	ServiceManagerService serviceManagerService;

	@RequestMapping(value="/servicemanagers",method=RequestMethod.POST)
	@ResponseBody
	public Object addServiceInfo(@RequestBody ServiceInfo serviceInfo){
			JSONObject jo = new JSONObject();
			Map<String, Object> requestError = null;
			try {
				serviceManagerService.save(serviceInfo);
				jo.put("success", "保存成功");
				Object object = JSON.toJSON(jo);
				WebUtils.getResponse().setStatus(200);
				return object;
			} catch (Exception e) {
				jo.put("error", "创建服务失败");
				requestError = ErrorUtil.getRequestError(jo);
				e.printStackTrace();
			}
			return requestError;
	}

	@RequestMapping(value="/servicemanagers/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Object getOneServiceInfo(@PathVariable("id") Integer id){
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			ServiceInfo byId = serviceManagerService.getById(id);
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

	@RequestMapping(value="/servicemanagers",method=RequestMethod.GET)
	@ResponseBody
	public Object listServiceInfo(){
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			List<ServiceInfo> listAll = serviceManagerService.listAll();
			jo.put("data", listAll);
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

	@RequestMapping(value="/servicemanagers/{idParam}",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateServiceInfo(@RequestBody ServiceInfo serviceInfo,@PathVariable("idParam") Integer idParam){

		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			serviceInfo.setId(idParam);
			serviceManagerService.update(serviceInfo);
			jo.put("success", "修改成功");
			Object object = JSON.toJSON(jo);
			WebUtils.getResponse().setStatus(200);
			return object;
		} catch (Exception e) {
			jo.put("error", "修改失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value="/servicemanagers/{id}/start",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateServiceStatuStart(@PathVariable("id") Integer id){

		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			serviceManagerService.updateServiceStatuStart(id);
			jo.put("success", "修改成功");
			Object object = JSON.toJSON(jo);
			WebUtils.getResponse().setStatus(200);
			return object;
		} catch (Exception e) {
			jo.put("error", "修改失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value="/servicemanagers/{id}/stop",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateServiceStatuStop(@PathVariable("id") Integer id){

		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			serviceManagerService.serviceUpdateStatuProcessing(id);
			jo.put("success", "修改成功");
			Object object = JSON.toJSON(jo);
			WebUtils.getResponse().setStatus(200);
			return object;
		} catch (Exception e) {
			jo.put("error", "修改失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}


	@RequestMapping(value="/servicemanagers/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public Object deleteServiceInfo(@PathVariable("id") Integer id){
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			serviceManagerService.deleteById(id);
			jo.put("success", "删除成功");
			Object object = JSON.toJSON(jo);
			WebUtils.getResponse().setStatus(200);
			return object;
		} catch (Exception e) {
			jo.put("error", "删除失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}


}
